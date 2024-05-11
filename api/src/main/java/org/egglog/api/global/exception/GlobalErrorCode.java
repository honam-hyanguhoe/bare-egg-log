package org.egglog.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorFormat {
    SERVER_ERROR("서버 에러 발생 죄송합니다.", INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}
