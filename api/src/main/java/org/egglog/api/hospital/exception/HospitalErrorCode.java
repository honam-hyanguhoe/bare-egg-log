package org.egglog.api.hospital.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HospitalErrorCode implements ErrorFormat {

    NOT_FOUND("존재하지않는 병원입니다.", HttpStatus.BAD_REQUEST),
    AUTH_NOT_FOUND("존재하지않는 병원인증 요청입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
