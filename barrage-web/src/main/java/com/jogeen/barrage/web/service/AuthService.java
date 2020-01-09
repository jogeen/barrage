package com.jogeen.barrage.web.service;

import com.jogeen.barrage.web.dao.AuthMapper;
import com.jogeen.barrage.web.dao.UserMapper;
import com.jogeen.barrage.web.model.Auth;
import com.jogeen.barrage.web.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    AuthMapper authMapper;


    public void insert(Auth auth) {
        authMapper.insertSelective(auth);
    }

    public Auth getByThridId(Integer id) {
        return  authMapper.getByThridId(id);
    }
}
