package org.egglog.api.worktype.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum WorkTypeErrorCode implements ErrorFormat {

    NO_EXIST_WORKTYPE("근무 타입이 존재하지 않습니다.", BAD_REQUEST),
    ACCESS_DENIED("권한이 없습니다.", BAD_REQUEST),
    NOT_SAME_WORKTYPE_ID("근무 아이디와 수정 근무 아이디와 다릅니다.", BAD_REQUEST);

    ;

    private final String message;
    private final HttpStatus httpStatus;
}
