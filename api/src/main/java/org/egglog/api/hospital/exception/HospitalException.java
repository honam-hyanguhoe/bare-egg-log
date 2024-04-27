package org.egglog.api.hospital.exception;

import lombok.Getter;
import org.egglog.api.group.exception.GroupErrorCode;
import org.egglog.utility.exception.BaseException;

@Getter
public class HospitalException extends BaseException {
    public HospitalException(HospitalErrorCode errorCode) {
        super(errorCode);
    }
}
