package org.egglog.api.calendargroup.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CalendarGroupErrorCode implements ErrorFormat {


    NOT_FOUND_CALENDAR_GROUP("존재하지 않는 캘린더 그룹입니다.", HttpStatus.BAD_REQUEST),

    DATABASE_CONNECTION_FAILED("데이터베이스 연결에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    TRANSACTION_ERROR("트랜잭션에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),

    ;

    private final String message;
    private final HttpStatus httpStatus;
}
