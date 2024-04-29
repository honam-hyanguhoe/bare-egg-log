package org.egglog.api.calendar.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class CalendarException extends BaseException {
    public CalendarException(CalendarErrorCode errorCode) {
        super(errorCode);
    }
}
