package org.egglog.utility.exception;

public abstract class BaseException extends RuntimeException{
    private final ErrorFormat errorCode;
    public BaseException(ErrorFormat errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
