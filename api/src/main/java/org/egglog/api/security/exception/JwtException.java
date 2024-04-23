package org.egglog.api.security.exception;


import lombok.Getter;
import org.egglog.utility.exception.BaseException;
import org.egglog.utility.exception.ErrorFormat;

@Getter
public class JwtException extends BaseException {

    public JwtException(ErrorFormat errorCode) {
        super(errorCode);
    }
}
