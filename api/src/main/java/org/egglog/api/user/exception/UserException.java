package org.egglog.api.user.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class UserException extends BaseException {
    public UserException(UserErrorCode errorCode){
        super(errorCode);
    }
}
