package com.sppart.admin.user.jwt.dto;

import com.sppart.admin.utils.Authority;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateTokenDto {

    private final String id;
    private final Authority authority;

    @Builder
    public CreateTokenDto(String id, Authority authority) {
        this.id = id;
        this.authority = authority;
    }
}
