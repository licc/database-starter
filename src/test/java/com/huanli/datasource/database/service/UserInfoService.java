package com.huanli.datasource.database.service;


import com.huanli.datasource.database.user.entity.UserDO;
import com.huanli.datasource.database.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInfoService {

    @Resource
    private UserMapper userDOMapper;

    public UserDO findModelById(Long id)  {

        return userDOMapper.findByMyId(id);

    }

}



