package com.yyq.mycommunity.mapper;

import com.yyq.mycommunity.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description, gmt_create,gmt_modified, creator, tag) value(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void creation(Question question);

    @Select("select * from question")
    List<Question> list();
}
