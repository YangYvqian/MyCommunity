package com.yyq.mycommunity.mapper;

import com.yyq.mycommunity.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

//    向数据库中插入获取的第三方登录的账号信息，包括name，token等
    @Insert("insert into user(name, account_id,token,gmt_create,gmt_modified) value(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);

}
