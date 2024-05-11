package com.sppart.admin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SuperpositionAdminException extends RuntimeException {

    private final ErrorCode errorCode;
}
