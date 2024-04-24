package org.egglog.api.alarm.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class AlarmException extends BaseException {

    public AlarmException(AlarmErrorCode errorCode) {
        super(errorCode);
    }
}
