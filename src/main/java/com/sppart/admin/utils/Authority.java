package com.sppart.admin.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {
    ROLE_ADMIN("관리자"),
    ROLE_MANAGER("매니저"),
    ROLE_GUEST("게스트");

    private final String value;
}
