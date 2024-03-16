package com.sppart.admin.utils;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {
    ROLE_ADMIN("관리자"),
    ROLE_MANAGER("매니저"),
    ROLE_GUEST("게스트"),
    NONE("NONE");

    private final String value;

    public static Authority findByName(String name) {
        return Arrays.stream(values())
                .filter(authority -> authority.name().equals(name))
                .findFirst()
                .orElse(NONE);
    }

    public boolean isNone() {
        return this == NONE;
    }
}
