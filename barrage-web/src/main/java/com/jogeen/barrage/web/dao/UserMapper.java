package com.jogeen.barrage.web.dao;

import com.jogeen.barrage.web.model.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    @Select("select * from tb_user where phone=#{phone}")
    public User getUserByPhone(String phone);

}
