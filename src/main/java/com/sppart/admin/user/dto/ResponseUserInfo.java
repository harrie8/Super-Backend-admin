package com.sppart.admin.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sppart.admin.utils.Authority;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseUserInfo {

    private final String id;
    @JsonIgnore
    private final String password;
    private final Authority authority;

    @Builder
    public ResponseUserInfo(String id, String password, Authority authority) {
        this.id = id;
        this.password = password;
        this.authority = authority;
    }
}
