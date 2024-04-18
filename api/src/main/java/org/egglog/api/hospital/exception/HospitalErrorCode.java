package org.egglog.api.hospital.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HospitalErrorCode {

    NOT_FOUND("존재하지않는 병원입니다.", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus httpStatus;
}
