package com.jogeen.barrage.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.jogeen.barrage.web.model.User;
import com.jogeen.barrage.web.response.Result;
import com.jogeen.barrage.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;


    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        User ruser = userService.getUserByPhone(user.getPhone());
        if (ruser.getPassword().equals(user.getPassword())) {
            setUserToSession(ruser);
            Object o = redisTemplate.opsForValue().get(ruser.getPhone());
            if (o == null) {
                redisTemplate.opsForValue().set(ruser.getPhone(), 8000L);
            }

            return new Result("登录成功",ruser.getType());
        }
        return Result.BuildFailedResult("用户名或密码错误");
    }


    @PostMapping("/register")
    public String register(@RequestBody User user) {
        userService.register(user);
        return null;
    }

    private void setUserToSession(User user) {
        httpServletRequest.getSession().setAttribute("user", new Gson().toJson(user));
    }
}
