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
    ALREADY_IN_EMAIL("이미 존재하는 이메일 입니다.", BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
