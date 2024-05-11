package com.sppart.admin.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Builder
public class Members {
    private int num;
    private String email;
    private String name;
    private boolean isAuthor;
    private Timestamp createAt;
    private Date updateAt;
}
