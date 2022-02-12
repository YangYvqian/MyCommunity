package com.yyq.mycommunity.service;

import com.yyq.mycommunity.mapper.UserMapper;
import com.yyq.mycommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    public void createOrUpdateUser(User user) {
        User dbuser = userMapper.findByAccountId(user.getAccountId());

        // 如果数据库没有存储user信息，则向数据库中插入信息
        if (dbuser == null){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            dbuser.setGmtModified(System.currentTimeMillis());
            dbuser.setAvatarUrl(user.getAvatarUrl());
            dbuser.setName(user.getName());
            dbuser.setToken(user.getToken());
            userMapper.updateUser(dbuser);
            // 更新user表
        }

    }
}
