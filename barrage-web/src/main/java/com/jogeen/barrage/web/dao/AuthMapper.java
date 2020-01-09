package com.jogeen.barrage.web.dao;

import com.jogeen.barrage.web.model.Auth;
import com.jogeen.barrage.web.model.Group;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface AuthMapper extends Mapper<Auth> {


    @Select("select * from tb_auth where third_id=#{id}")
    Auth getByThridId(Integer id);
}
