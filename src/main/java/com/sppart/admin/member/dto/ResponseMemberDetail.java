package com.sppart.admin.member.dto;

import com.sppart.admin.utils.Gender;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ResponseMemberDetail {
    private int num;
    private String email;
    private Gender gender;
    private Date birth;
    private String image;
    private String userType;
    private Date createAt;
    private Date updateAt;
    private AuthorInfo authorInfo;
}
