package com.sppart.admin.user.domain;

public enum Role {
    ADMIN,
    MANAGER,
    GUEST,
    ;

    public boolean equals(Role other) {
        return this == other;
    }
}