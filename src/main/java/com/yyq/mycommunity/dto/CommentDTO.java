package com.yyq.mycommunity.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Integer parentId;
    private String comment;
    private Integer type;
}
