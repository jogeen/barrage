package com.jogeen.barrage.web.controller;


import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.jogeen.barrage.common.GiftEnum;
import com.jogeen.barrage.common.Message;
import com.jogeen.barrage.common.MessageFactory;
import com.jogeen.barrage.web.config.CurrentStatus;
import com.jogeen.barrage.web.mina.ProtocolClient;
import com.jogeen.barrage.web.model.Group;
import com.jogeen.barrage.web.model.User;
import com.jogeen.barrage.web.request.Answer;
import com.jogeen.barrage.web.request.GiftRequest;
import com.jogeen.barrage.web.request.MessageRequest;
import com.jogeen.barrage.web.request.Question;
import com.jogeen.barrage.web.response.Result;
import com.jogeen.barrage.web.vo.GroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/barrage")
public class BarrageController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    private ProtocolClient protocolClient;

    RateLimiter rateLimiter_level1 = RateLimiter.create(200);

    RateLimiter rateLimiter_level2 = RateLimiter.create(300);

    @PostMapping("/message")
    public Result sendMessage(@RequestBody MessageRequest messageRequest) {
        String message = messageRequest.getMessage();
        if("JOGEENHELP".equals(message)){
            return new Result(300,"恭喜你，发现彩蛋入口，想办法请求所有你能看到的接口吧！看看接口是否有bug或者惊喜！",null);
        }
        Message textMessage = MessageFactory.createTextMessage(messageRequest.getMessage(), getUserFronSession().getUsername());
        String result = protocolClient.sendData(textMessage);
        return new Result(result);
    }

    @PostMapping("/gift")
    public String sendGift(@RequestBody GiftRequest giftRequest) {
        GiftEnum gitf = GiftEnum.getGiftByType(giftRequest.getType());
        if(gitf==null){
            return "无效的礼物类型";
        }
        String phone = getUserFronSession().getPhone();
        String value = (String) redisTemplate.boundValueOps(phone).get();
        if (gitf.getValue() > Long.parseLong(value)) {
            return "积分不足,去充值吧";
        }
        Long decrement = redisTemplate.boundValueOps(phone).decrement(gitf.getValue());
        if (decrement < 0) {
            redisTemplate.boundValueOps(phone).increment(gitf.getValue());
            return "积分不足,去充值吧";
        }

        if (CurrentStatus.CURRENT_GROUP != null) {
            setIncrementHashValue("group_value",""+ CurrentStatus.CURRENT_GROUP.getId(), gitf.getValue());
        }

        Message giftMessage = MessageFactory.createGiftMessage(gitf, getUserFronSession().getUsername());
        return protocolClient.sendData(giftMessage);
    }

    @GetMapping("/refresh")
    public Map refresh() {
        Map result=new HashMap();
        result.put("currentGroup", CurrentStatus.CURRENT_GROUP);
        String value = (String) redisTemplate.boundValueOps(getUserFronSession().getPhone()).get();
        result.put("score",Long.parseLong(value));
        return result;
    }


    @GetMapping("/rank")
    public List list() {
        Set<String> keys = hashKeys("group_value");
        List<Group> group_info = redisTemplate.boundHashOps("group_info").values();
        List<GroupVo> groupVoList=new ArrayList<>();
        for (int i = 0; i < group_info.size(); i++) {
            Group group = group_info.get(i);
            GroupVo groupVo=new GroupVo();
            BeanUtils.copyProperties(group,groupVo);
            groupVo.setScore(getRedisHashValue("group_value",""+group.getId()));
            groupVoList.add(groupVo);
        }
        Collections.sort(groupVoList, new Comparator<GroupVo>() {
            @Override
            public int compare(GroupVo o1, GroupVo o2) {
                return o1.getScore()<o2.getScore()?1:-1;
            }
        });
        for (int i = 0; i < groupVoList.size(); i++) {
            GroupVo temp = groupVoList.get(i);
            if(temp.getScore()==0){
                temp.setPercentage(temp.getScore());
            }else{
                temp.setPercentage(((temp.getScore()*100)/(groupVoList.get(0).getScore())));
            }
        }

        return groupVoList;
    }

    @GetMapping("/question")
    public Result getQuestion() {
        if (CurrentStatus.CURRENT_QUESTION_ID != null) {
            Question question = new Question();
            BeanUtils.copyProperties(CurrentStatus.CURRENT_QUESTION, question);
            question.setAnswer("");
            question.setAnalysis("");
            return new Result("获取成功", question);
        } else {
            return Result.BuildFailedResult("当前没有题目");
        }
    }

    @PostMapping("/answer")
    public Result answerQuestion(@RequestBody Answer answer) {
        if (StringUtils.isEmpty(answer.getAnswerOption())) {
            return Result.BuildFailedResult("请选择答案");
        }
        String questionStr = (String) redisTemplate.boundValueOps("Current_Question_" + CurrentStatus.CURRENT_QUESTION_ID).get();
        if (questionStr == null) {
            CurrentStatus.CURRENT_QUESTION_ID = null;
            return Result.BuildFailedResult("本次答题已结束！");
        }
        Question question = new Gson().fromJson(questionStr, Question.class);
        if(checkEgg(answer.getAnswerOption())){
            if(redisTemplate.opsForSet().isMember("Egg01",getUserFronSession().getPhone())){
                return new Result("继续寻找第二彩蛋吧！");
            }
            redisTemplate.opsForSet().add("Egg01",getUserFronSession().getPhone());
            redisTemplate.opsForSet().add("Answer_List_"+question.getId(),getUserFronSession().getPhone());
            Long increment = redisTemplate.boundValueOps(getUserFronSession().getPhone()).increment(10000L);
            return new Result(300,"恭喜你发现第一个彩蛋，你没有拘泥于前端界面，尝试了模拟请求并且修改了参数，送你10000金币作为奖励。大彩蛋就在/barrage/egg接口！加油吧",null);
        }
        if(redisTemplate.opsForSet().isMember("Answer_List_"+question.getId(),getUserFronSession().getPhone())){
            return Result.BuildFailedResult("本次已经作答！");
        }
        if (question.getAnswer().equals(answer.getAnswerOption())) {
            redisTemplate.opsForSet().add("Answer_List_"+question.getId(),getUserFronSession().getPhone());
            Long increment = redisTemplate.boundValueOps(getUserFronSession().getPhone()).increment(question.getScore());
            return new Result("回答正确，加" + question.getScore() + "金币", CurrentStatus.CURRENT_QUESTION);
        } else {
            return Result.BuildFailedResult("回答错误", CurrentStatus.CURRENT_QUESTION);
        }

    }

    @GetMapping("/egg")
    public  Result egg(){
        if(CurrentStatus.isEgg){
            return Result.BuildFailedResult("彩蛋已经被人拿走了！");
        }
        System.out.println(getUserFronSession().getUsername()+"正在获取冲击第1层");
        if(rateLimiter_level1.tryAcquire()){
            return Result.BuildFailedResult("还不够快!!!");
        }else if( rateLimiter_level2.tryAcquire()){
            System.out.println(getUserFronSession().getUsername()+"正在获取冲击第2层");
            return Result.BuildFailedResult("冲破第一层,叫上你的小伙伴一起来吧！");
        }else{
            synchronized (this){
                if(!CurrentStatus.isEgg){
                    CurrentStatus.isEgg=true;
                    Long increment = redisTemplate.boundValueOps(getUserFronSession().getPhone()).increment(1000000L);
                    redisTemplate.boundValueOps("lucky").set(getUserFronSession().getUsername());
                    return new Result("发太快，描述估计你也看不清了。恭喜你获得最大的彩蛋，奖励你1000000金币。如果你能看清，来找根哥，用520133这串数字来换取神秘大奖！！");
                }else{
                    return Result.BuildFailedResult("彩蛋已经被人拿走了！");
                }
            }
        }
    }




    private User getUserFronSession() {
        String user = (String) httpServletRequest.getSession().getAttribute("user");
        if (StringUtils.isEmpty(user)) {
            return null;
        }
        return new Gson().fromJson(user, User.class);
    }

    private Long getRedisHashValue(String key,String Hashkey){
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        byte[] bytes = connection.hGet(key.getBytes(), Hashkey.getBytes());
        if(bytes==null||bytes.length==0){
            return 0L;
        }
        return Long.parseLong(new String(bytes));
    }

    private Long setIncrementHashValue(String key,String Hashkey,Long value){
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        return connection.hIncrBy(key.getBytes(), Hashkey.getBytes(), value);
    }
    private Set<String> hashKeys(String key){
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        Set<byte[]> byteSet = connection.hKeys(key.getBytes());
        Set<String> keys=new HashSet<>();
        for (byte[] bytes : byteSet) {
            String s = new String(bytes);
            keys.add(s);
        }
        return keys;
    }
    private boolean checkEgg(String answer){
        if(!StringUtils.isEmpty(answer)&&!"A".equals(answer.toUpperCase())&&!"B".equals(answer.toUpperCase())&&!"C".equals(answer.toUpperCase())&&!"D".equals(answer.toUpperCase())){
            return true;
        }
        return  false;
    }

}
