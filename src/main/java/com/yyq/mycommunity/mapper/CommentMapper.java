package com.yyq.mycommunity.mapper;

import com.yyq.mycommunity.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment(parent_id,type,commentator,gmt_create,gmt_modified,comment) value(#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{comment})")
    void insert(Comment comment);

    @Select("select * from comment where parent_id=#{parentId}")
    Comment geCommentByParentId(Integer parentId);


}
