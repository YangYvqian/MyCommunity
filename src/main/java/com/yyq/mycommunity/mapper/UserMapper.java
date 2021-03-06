package com.yyq.mycommunity.mapper;

import com.yyq.mycommunity.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

//    向数据库中插入获取的第三方登录的账号信息，包括name，token等
    @Insert("insert into user(name, account_id,token,gmt_create,gmt_modified,avatar_url) value(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(int id);

    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name=#{name}, token=#{token}, gmt_modified=#{gmtModified}, avatar_url=#{avatarUrl} where id=#{id}")
    void updateUser(User user);
}
