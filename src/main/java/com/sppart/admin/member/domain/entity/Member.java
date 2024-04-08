package com.sppart.admin.member.domain.entity;

import com.sppart.admin.utils.Gender;

import java.sql.Timestamp;

public class Member {
    private String email;
    private String name;
    private String nickname;
    private String profile;
    private Gender gender;
    private int birthYear;
    private boolean isArtis;
    private Timestamp createAt;
    private Timestamp deleteAt;
    private Timestamp nicknameUpdateAt;
    private boolean isActive;
}
