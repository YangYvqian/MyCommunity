package com.yyq.mycommunity.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class User {
    private Integer id;
    private  String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
}
