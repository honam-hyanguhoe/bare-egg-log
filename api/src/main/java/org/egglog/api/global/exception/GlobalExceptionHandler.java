package org.egglog.api.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.egglog.utility.exception.BaseException;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity baseExceptionHandler(BaseException e){
        log.error("==============================[--예외 처리--]=============================");
        log.error("[e.getMessage] : {}", e.getMessage());
        log.error("[e.getErrorCode().getHttpStatus()] : {}", e.getErrorCode().getHttpStatus());
        log.error("[e.getErrorCode().getMessage()] : {}",e.getErrorCode().getMessage());
        log.debug("==============================[Stack trace]=============================");
        log.debug(Arrays.toString(e.getStackTrace()));
        log.debug("======================================================================");
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(MessageUtils.fail(String.valueOf(e.getErrorCode()),e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageUtils> CommentExceptionHandler(Exception e) {
        log.error("============================[예상하지 못한 예외 발생]===========================");
        log.error("[e.toString()] : {}", e.toString());
        log.error("[e.getMessage] : {}", e.getMessage());
        log.debug("==============================[Stack trace]=============================");
        log.debug(Arrays.toString(e.getStackTrace()));
        log.debug("=========================================================================");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(MessageUtils.fail(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
    }
}
