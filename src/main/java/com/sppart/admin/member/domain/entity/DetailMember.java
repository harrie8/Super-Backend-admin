package com.sppart.admin.member.domain.entity;

import com.sppart.admin.member.dto.AuthorInfo;
import com.sppart.admin.member.dto.ResponseMemberDetail;
import com.sppart.admin.utils.Gender;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class DetailMember {
    private String email;
    private Gender gender;
    private String name;
    private Date birth;
    private String profile;
    private boolean isArtist;
    private Date createAt;
    private Date updateAt;
    //추후 현재 안 됨
    private String instagramId; //인스타 ID
    private String artistName;
}
