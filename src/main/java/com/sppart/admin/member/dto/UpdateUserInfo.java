package com.sppart.admin.member.dto;

import com.sppart.admin.utils.Gender;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Builder
public class UpdateUserInfo {
    private String email;
    private String nickname;
    private Gender gender;
    private Date birth;
    private MultipartFile image;
}
