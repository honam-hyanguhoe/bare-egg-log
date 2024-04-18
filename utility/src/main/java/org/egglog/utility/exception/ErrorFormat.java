package org.egglog.utility.exception;
import org.springframework.http.HttpStatus;

public interface ErrorFormat {
    HttpStatus getHttpStatus();
    String getMessage();
}
