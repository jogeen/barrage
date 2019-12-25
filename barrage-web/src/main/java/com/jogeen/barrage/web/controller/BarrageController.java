package com.jogeen.barrage.web.controller;


import com.google.gson.Gson;
import com.jogeen.barrage.common.GiftEnum;
import com.jogeen.barrage.common.Message;
import com.jogeen.barrage.common.MessageFactory;
import com.jogeen.barrage.web.config.Status;
import com.jogeen.barrage.web.mina.ProtocolClient;
import com.jogeen.barrage.web.model.Group;
import com.jogeen.barrage.web.model.User;
import com.jogeen.barrage.web.request.GiftRequest;
import com.jogeen.barrage.web.request.MessageRequest;
import com.jogeen.barrage.web.vo.GroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
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

    @PostMapping("/message")
    public String sendMessage(@RequestBody MessageRequest messageRequest) {
        Message textMessage = MessageFactory.createTextMessage(messageRequest.getMessage(), getUserFronSession().getUsername());
        return protocolClient.sendData(textMessage);
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

        if (Status.CURRENT_GROUP != null) {
            setIncrementHashValue("group_value",""+Status.CURRENT_GROUP.getId(), gitf.getValue());
        }

        Message giftMessage = MessageFactory.createGiftMessage(gitf, getUserFronSession().getUsername());
        return protocolClient.sendData(giftMessage);
    }

    @GetMapping("/refresh")
    public Map refresh() {
        Map result=new HashMap();
        List list = list();
        result.put("groupList",list);
        result.put("currentGroup",Status.CURRENT_GROUP);
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

}
