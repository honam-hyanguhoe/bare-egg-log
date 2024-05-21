package org.egglog.api.work.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum WorkErrorCode implements ErrorFormat {
    NOT_EXISTS_USER("존재하지 않는 근무 일정 입니다.", BAD_REQUEST),
    FORMAT_NOT_SUPPORTED("지원되지 않는 포멧 입니다.", BAD_REQUEST),
    ACCESS_DENIED("그룹 권한이 없습니다.", BAD_REQUEST),
    ALREADY_IN_EMAIL("이미 존재하는 근무 일정 입니다.", BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}