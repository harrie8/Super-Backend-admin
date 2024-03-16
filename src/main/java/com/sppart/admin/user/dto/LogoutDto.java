package com.sppart.admin.user.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class LogoutDto {

    private final String id;
    private final String rt;

    @Builder
    public LogoutDto(String id, String rt) {
        this.id = id;
        this.rt = rt;
    }

    public boolean isValidRt() {
        return StringUtils.hasText(rt);
    }
}
