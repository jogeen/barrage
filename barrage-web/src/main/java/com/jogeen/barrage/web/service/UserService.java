package com.jogeen.barrage.web.service;

import com.jogeen.barrage.web.dao.UserMapper;
import com.jogeen.barrage.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void register(User user) {
        userMapper.insert(user);
    }

    public User getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    public User getUserById(Integer uid) {
        return userMapper.selectByPrimaryKey(uid);
    }
}
