package com.sppart.admin.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Users {
    private final String id;
    @JsonIgnore
    private final String password;
    private final Role role;

    @Builder
    public Users(String id, String password, Role role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }
}
