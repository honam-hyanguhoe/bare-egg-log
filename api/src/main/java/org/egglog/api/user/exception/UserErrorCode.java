package org.egglog.api.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorFormat {
    NOT_EXISTS_USER("존재하지 않는 회원입니다.", BAD_REQUEST),
    TRANSACTION_FAIL("데이터 조회에 실패했습니다.", BAD_REQUEST),
    DELETED_USER("탈퇴한 회원입니다.", BAD_REQUEST),
    INACTIVE_USER("휴먼 회원입니다.", BAD_REQUEST),
    ACCESS_DENIED("권한이 없습니다.", BAD_REQUEST),
    ALREADY_IN_EMAIL("이미 존재하는 이메일 입니다.", BAD_REQUEST),
    JOIN_USER("이미 가입한 회원입니다.", BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
