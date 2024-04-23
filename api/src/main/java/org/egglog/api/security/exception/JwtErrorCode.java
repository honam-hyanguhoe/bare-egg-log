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
    NOT_EXISTS_TOKEN("유효하지 않은 정보입니다.", BAD_REQUEST),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", UNAUTHORIZED),
    INVALID_TOKEN("사용할 수 없는 토큰 입니다.", UNAUTHORIZED),
    TOKEN_SIGNATURE_ERROR("잘못된 토큰", UNAUTHORIZED),
    NOT_SUPPORT_TOKEN("지원되지 않는 토큰", UNAUTHORIZED),
    NO_TOKEN("토큰이 없습니다.", UNAUTHORIZED);

    private final String message;
    private final HttpStatus httpStatus;
}

