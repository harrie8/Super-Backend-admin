package com.sppart.admin.main.product.exception;

import com.sppart.admin.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductPageErrorCode implements ErrorCode {

    INVALID_PAGE_AND_SIZE_PARAMETER(HttpStatus.BAD_REQUEST, "page 파라미터와 size 파라미터는 함께 전송해야 합니다."),
    INVALID_PAGE_PARAMETER(HttpStatus.BAD_REQUEST, "page 파라미터는 1보다 크거나 같아야 합니다."),
    INVALID_SIZE_PARAMETER(HttpStatus.BAD_REQUEST, "size 파라미터는 1보다 크거나 같아야 합니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "page 파라미터 또는 size 파라미터가 null 입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
