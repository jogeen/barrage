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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            redisTemplate.boundHashOps("group_value").increment(Status.CURRENT_GROUP.getId(), gitf.getValue());
        }

        Message giftMessage = MessageFactory.createGiftMessage(gitf, getUserFronSession().getUsername());
        return protocolClient.sendData(giftMessage);
    }


    @GetMapping("/rank")
    public List list() {
        Set<String> keys = redisTemplate.boundHashOps("group_value").keys();
        List<Group> group_info = redisTemplate.boundHashOps("group_info").values();
        for (int i = 0; i < group_info.size(); i++) {
            Group group = group_info.get(i);
            group.setValue((Long) redisTemplate.boundHashOps("group_value").get(group.getId()));
        }

        return group_info;
    }


    private User getUserFronSession() {
        String user = (String) httpServletRequest.getSession().getAttribute("user");
        if (StringUtils.isEmpty(user)) {
            return null;
        }
        return new Gson().fromJson(user, User.class);
    }

}
