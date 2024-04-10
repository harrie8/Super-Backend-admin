package com.sppart.admin.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel
@Getter
@NoArgsConstructor
public class LoginRequest {

    @ApiModelProperty(value = "회원 ID", required = true)
    @NotBlank
    private String id;
    @ApiModelProperty(value = "회원 PASSWORD", required = true)
    @NotBlank
    private String password;

    @Builder
    public LoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
