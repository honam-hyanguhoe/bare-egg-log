package org.egglog.api.alarm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.api.board.exception.BoardErrorCode;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AlarmErrorCode implements ErrorFormat {

    NO_EXIST_ALARM("알람이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    DATABASE_CONNECTION_FAILED("데이터베이스 연결에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    TRANSACTION_ERROR("트랜잭션에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
    ;

    private final String message;
    private HttpStatus httpStatus;
}
