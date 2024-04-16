//package org.egglog.api.global.exception;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.Arrays;
//
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(UserException.class)
//    public ResponseEntity userExceptionHandler(UserException e){
//        log.debug(Arrays.toString(e.getStackTrace()));
//        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
//                .body(MessageUtils.fail(String.valueOf(e.getErrorCode()),e.getMessage()));
//    }
//
//    @ExceptionHandler(AuthException.class)
//    public ResponseEntity authExceptionHandler(AuthException e){
//        log.debug(Arrays.toString(e.getStackTrace()));
//        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
//                .body(MessageUtils.fail(String.valueOf(e.getErrorCode()),e.getMessage()));
//    }
//
//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity JwtExceptionHandler(JwtException e){
//        log.debug(Arrays.toString(e.getStackTrace()));
//        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
//                .body(MessageUtils.fail(String.valueOf(e.getErrorCode()),e.getMessage()));
//    }
//
//    @ExceptionHandler(GroupException.class)
//    public ResponseEntity<MessageUtils> GroupExceptionHandler(GroupException e) {
//        log.debug(Arrays.toString(e.getStackTrace()));
//        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
//                .body(MessageUtils.fail(String.valueOf(e.getErrorCode()), e.getMessage()));
//    }
//
//    @ExceptionHandler(FcmException.class)
//    public ResponseEntity<MessageUtils> FcmExceptionHandler(FcmException e) {
//        log.debug(Arrays.toString(e.getStackTrace()));
//        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
//                .body(MessageUtils.fail(e.getErrorCode().name(), e.getMessage()));
//    }
//
//    @ExceptionHandler(BoardException.class)
//    public ResponseEntity<MessageUtils> BoardExceptionHandler(BoardException e) {
//        log.debug(Arrays.toString(e.getStackTrace()));
//        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
//                .body(MessageUtils.fail(e.getErrorCode().name(), e.getMessage()));
//    }
//
//    @ExceptionHandler(CommentException.class)
//    public ResponseEntity<MessageUtils> CommentExceptionHandler(CommentException e) {
//        log.debug(Arrays.toString(e.getStackTrace()));
//        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
//                .body(MessageUtils.fail(e.getErrorCode().name(), e.getMessage()));
//    }
//}
