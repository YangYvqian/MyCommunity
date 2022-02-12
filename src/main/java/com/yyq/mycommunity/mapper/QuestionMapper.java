package com.yyq.mycommunity.mapper;

import com.yyq.mycommunity.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    // 获取前端发布的问题，并插入到数据库
    @Insert("insert into question(title,description, gmt_create,gmt_modified, creator, tag) value(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void creation(Question question);

    // 获取每页问题列表
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset,
                        @Param(value = "size")Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId") Integer userId,
                        @Param(value = "offset") Integer offset,
                        @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator=#{userId}")
    Integer countByUserId(@Param(value = "userId") Integer userId);

    @Select("select * from question where id = #{questionId}")
    Question getQuestionByQuestionId(Integer questionId);

    @Select("select * from question where id = #{id}")
    Question getQuestionById(Integer id);

    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag} where id = #{id}")
    void updateQuestion(Question question);
}
