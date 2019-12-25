package com.jogeen.barrage.web.controller;

import com.jogeen.barrage.common.MessageFactory;
import com.jogeen.barrage.web.config.Status;
import com.jogeen.barrage.web.mina.ProtocolClient;
import com.jogeen.barrage.web.model.Group;
import com.jogeen.barrage.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @GetMapping("/connetion/{ip}/{port}/{grouid}")
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

    @GetMapping("/test")
    public String test() {
        protocolClient.sendData(MessageFactory.createTextMessage("管理员服务器连接测试", "system"));
        return "测试成功";
    }
}
