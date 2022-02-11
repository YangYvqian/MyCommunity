package com.yyq.mycommunity.dto;

import com.yyq.mycommunity.model.User;
import lombok.Data;

/**
 * 有Question实体类了，为什么还要写QuestionDTO呢？
 * 因为Question实体类对应数据库question表，但现在是需要其他表中的信息，如果不用联合查询，怎么获取到不同表中的数据呢？
 * 现在的需求是要获取question和user两个表中的数据
 * 所以定义一个数据传输对象，实现传输两个表中的数据，也就是组合question表和user表中的数据，并作为结果进行返回。
 * **/

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private User user;
}
