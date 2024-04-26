package org.egglog.api.calendar.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EventErrorCode implements ErrorFormat {

    NOT_FOUND_EVENT("존재하지 않는 일정입니다.", HttpStatus.BAD_REQUEST),

    DATABASE_CONNECTION_FAILED("데이터베이스 연결에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    TRANSACTION_ERROR("트랜잭션에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),

    ;

    private final String message;
    private final HttpStatus httpStatus;

}