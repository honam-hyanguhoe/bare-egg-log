package org.egglog.api.worktype.exception;

import lombok.Getter;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.utility.exception.BaseException;

@Getter
public class WorkTypeException extends BaseException {

    public WorkTypeException(WorkTypeErrorCode errorCode) {
        super(errorCode);
    }
}
