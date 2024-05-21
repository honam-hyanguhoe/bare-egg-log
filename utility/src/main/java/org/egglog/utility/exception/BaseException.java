package org.egglog.utility.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException{
    private final ErrorFormat errorCode;
    public BaseException(ErrorFormat errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
