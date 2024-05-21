package org.egglog.api.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Getter
@AllArgsConstructor
public enum JwtErrorCode implements ErrorFormat {
    NOT_EXISTS_TOKEN("유효하지 않은 정보입니다.", UNAUTHORIZED),
    EXPIRED_ACCESS_TOKEN("토큰이 만료되었습니다.", UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("사용할 수 없는 토큰 입니다. 다시 로그인 해주세요.", GONE),
    NO_TOKEN_FOR_TEST("토큰이 없습니다.", BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}

