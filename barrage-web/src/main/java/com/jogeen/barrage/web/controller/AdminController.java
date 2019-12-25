package com.jogeen.barrage.web.controller;

import com.jogeen.barrage.common.MessageFactory;
import com.jogeen.barrage.web.config.Status;
import com.jogeen.barrage.web.mina.ProtocolClient;
import com.jogeen.barrage.web.model.Group;
import com.jogeen.barrage.web.model.User;
import com.jogeen.barrage.web.request.RechangeRequest;
import com.jogeen.barrage.web.service.GroupService;
import com.jogeen.barrage.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    public String connectAnchor(@PathVariable("ip") String ip, @PathVariable("port") Integer port, @PathVariable("grouid") Integer grouid) {
        Group group = groupService.queryGroupById(grouid);
        if (group == null) {
            return "连接失败，组号不存在";
        }
        try {
            protocolClient.initClient(ip, port);
        } catch (Exception e) {
            return "连接失败";
        }
        redisTemplate.boundHashOps("group_info").put(group.getId(),group);
        Status.CURRENT_GROUP = group;
        return "连接成功";
    }
    @PostMapping("/recharge")
    public String recharge(@RequestBody RechangeRequest rechangeRequest) {
        if(StringUtils.isEmpty(rechangeRequest.getPhone())||StringUtils.isEmpty(rechangeRequest.getValue())){
            return "参数为空";
        }
        User userByPhone = userService.getUserByPhone(rechangeRequest.getPhone());
        if(userByPhone==null){
            return "用户不存在";
        }
        Long increment = redisTemplate.boundValueOps(rechangeRequest.getPhone()).increment(rechangeRequest.getValue());
        return "充值成功成功，当前金币数："+increment;
    }


    @GetMapping("/test")
    public String test() {
        return protocolClient.sendData(MessageFactory.createTextMessage("管理员服务器连接测试", "system"));
    }

}
