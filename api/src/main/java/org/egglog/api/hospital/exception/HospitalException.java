package org.egglog.api.hospital.exception;

import lombok.Getter;
import org.egglog.api.group.exception.GroupErrorCode;

@Getter
public class HospitalException extends RuntimeException {

    private final HospitalErrorCode errorCode;

    public HospitalException(HospitalErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
