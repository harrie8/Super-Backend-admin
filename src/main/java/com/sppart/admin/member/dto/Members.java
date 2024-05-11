package com.sppart.admin.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class Members {
    private int num;
    private String email;
    private String name;
    private boolean isAuthor;
    private Date createAt;
    private Date updateAt;
}
