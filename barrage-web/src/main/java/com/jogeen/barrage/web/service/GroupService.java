package com.jogeen.barrage.web.service;

import com.jogeen.barrage.web.dao.GroupMapper;
import com.jogeen.barrage.web.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;

    public Group queryGroupById(Integer grouid) {
        return groupMapper.selectByPrimaryKey(grouid);

    }
}
