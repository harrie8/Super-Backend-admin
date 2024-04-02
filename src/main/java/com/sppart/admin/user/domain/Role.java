package com.sppart.admin.user.domain;

public enum Role {
    SUPER_ADMIN,
    ADMIN,
    GUEST,
    ;

    public boolean equals(Role other) {
        return this == other;
    }
}