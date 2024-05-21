package org.egglog.api.calendar.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CalendarErrorCode implements ErrorFormat {
    DATABASE_CONNECTION_FAILED("데이터베이스 연결에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    ICS_SYNC_FAIL("ICS 연동에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IS_NOT_ICS("올바르지 않은 ICS 링크 입니다..", HttpStatus.BAD_REQUEST),
    CREATE_FAIL("캘린더 생성에 실패했습니다(ics).",HttpStatus.INTERNAL_SERVER_ERROR),
    SCHEDULE_NOT_FOUND("일정이 존재하지 않습니다.",HttpStatus.BAD_REQUEST);
    private final String message;
    private final HttpStatus httpStatus;
}
