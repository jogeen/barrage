package com.jogeen.barrage.web.controller;

import com.google.gson.Gson;
import com.jogeen.barrage.common.MessageFactory;
import com.jogeen.barrage.web.config.CurrentStatus;
import com.jogeen.barrage.web.mina.ProtocolClient;
import com.jogeen.barrage.web.model.Group;
import com.jogeen.barrage.web.model.User;
import com.jogeen.barrage.web.request.Answer;
import com.jogeen.barrage.web.request.Question;
import com.jogeen.barrage.web.request.RechangeRequest;
import com.jogeen.barrage.web.response.Result;
import com.jogeen.barrage.web.service.GroupService;
import com.jogeen.barrage.web.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProtocolClient protocolClient;

    @Autowired
    GroupService groupService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserService userService;

    @GetMapping("/conn/{ip}/{port}/{grouid}")
    public Result connectAnchor(@PathVariable("ip") String ip, @PathVariable("port") Integer port, @PathVariable("grouid") Integer grouid) {
        Group group = groupService.queryGroupById(grouid);
        if (group == null) {
            return Result.BuildFailedResult("连接失败，组号不存在");
        }
        try {
            protocolClient.initClient(ip, port);
        } catch (Exception e) {
            return Result.BuildFailedResult("连接失败");
        }
        redisTemplate.boundHashOps("group_info").put(group.getId(), group);
        CurrentStatus.CURRENT_GROUP = group;
        return new Result("连接成功");
    }

    @PostMapping("/recharge")
    public Result recharge(@RequestBody RechangeRequest rechangeRequest) {
        if (StringUtils.isEmpty(rechangeRequest.getPhone()) || StringUtils.isEmpty(rechangeRequest.getValue())) {
            return Result.BuildFailedResult("参数为空");
        }
        User userByPhone = userService.getUserByPhone(rechangeRequest.getPhone());
        if (userByPhone == null) {
            return Result.BuildFailedResult("用户不存在");
        }
        Long increment = redisTemplate.boundValueOps(rechangeRequest.getPhone()).increment(rechangeRequest.getValue());
        return new Result("充值成功成功，当前金币数：" + increment);
    }


    @GetMapping("/test")
    public String test() {
        return protocolClient.sendData(MessageFactory.createTextMessage("管理员服务器连接测试", "system"));
    }


    @PostMapping("/publish")
    public Result publish(@RequestBody Question question) {
        if(!checkAnswerSetting(question)){
            return Result.BuildFailedResult("正确答案设置有误！只能是ABCD四个选项");
        }
        CurrentStatus.CURRENT_QUESTION_ID = UUID.randomUUID().toString();
        question.setId(CurrentStatus.CURRENT_QUESTION_ID);
        CurrentStatus.CURRENT_QUESTION = question;
        redisTemplate.boundValueOps("Current_Question_" + CurrentStatus.CURRENT_QUESTION_ID).set(new Gson().toJson(CurrentStatus.CURRENT_QUESTION), question.getTimeLimit(), TimeUnit.SECONDS);
        redisTemplate.boundListOps("History_Question").rightPush(new Gson().toJson(CurrentStatus.CURRENT_QUESTION));
        return new Result("发布成功");

    }
    
    private boolean checkAnswerSetting(Question question){
        String answer = question.getAnswer();
        if(answer==null){
            return false;
        }
        if(!"A".equals(answer.toUpperCase())&&!"B".equals(answer.toUpperCase())&&!"C".equals(answer.toUpperCase())&&!"D".equals(answer.toUpperCase())){
            return false;
        }
        return true;
    }


}
