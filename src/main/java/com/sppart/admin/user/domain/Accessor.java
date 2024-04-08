package com.sppart.admin.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Accessor {

    private final String id;
    private final Role role;

    @Builder
    public Accessor(String id, Role role) {
        this.id = id;
        this.role = role;
    }
}
