package org.egglog.api.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardErrorCode implements ErrorFormat {

    NO_EXIST_BOARD("존재하지 않는 게시글입니다.", HttpStatus.BAD_REQUEST),
    NO_EXIST_CATEGORY("존재하지 않는 게시판 카테코리입니다.", HttpStatus.BAD_REQUEST),
    TRANSACTION_ERROR("트랜잭션에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_SAME_WRITER("작성자가 아닙니다.",  HttpStatus.BAD_REQUEST),


    DATABASE_CONNECTION_FAILED("데이터베이스 연결에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_ERROR("미등록 에러가 발생했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),

    //좋아요
    NO_EXIST_BOARD_LIKE("좋아요를 누르지 않았습니다", HttpStatus.BAD_REQUEST),
    ALREADY_LIKE("이미 좋아요를 누른 게시물입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private HttpStatus httpStatus;
}
