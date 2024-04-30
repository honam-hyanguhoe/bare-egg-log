package org.egglog.api.work.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class WorkException extends BaseException{
    public WorkException(WorkErrorCode errorCode){
        super(errorCode);
    }

}