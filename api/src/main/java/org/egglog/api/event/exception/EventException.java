package org.egglog.api.event.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class EventException extends BaseException {

    public EventException(EventErrorCode errorCode){
        super(errorCode);
    }

}
