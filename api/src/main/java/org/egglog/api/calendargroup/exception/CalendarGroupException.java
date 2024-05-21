package org.egglog.api.calendargroup.exception;

import lombok.Getter;
import org.egglog.api.event.exception.EventErrorCode;
import org.egglog.utility.exception.BaseException;

@Getter
public class CalendarGroupException extends BaseException {

    public CalendarGroupException(CalendarGroupErrorCode errorCode){
        super(errorCode);
    }
}
