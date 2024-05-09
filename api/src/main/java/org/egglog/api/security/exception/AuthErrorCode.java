package org.egglog.api.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
@Getter
@AllArgsConstructor
public enum AuthErrorCode {
    PLZ_MORE_INFO("회원가입 및 추가 정보가 더 필요합니다.", I_AM_A_TEAPOT),
    DELETE_USER("삭제된 회원입니다.", NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;
}
