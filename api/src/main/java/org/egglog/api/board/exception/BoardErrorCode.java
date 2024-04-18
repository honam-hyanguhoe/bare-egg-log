package org.egglog.api.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardErrorCode {
    NO_EXIST_BOARD("존재하지 않는 게시글입니다.", HttpStatus.BAD_REQUEST),
    NO_EXIST_CATEGORY("존재하지 않는 게시판 카테코리입니다.", HttpStatus.BAD_REQUEST),
    TRANSACTION_ERROR("트랜잭션에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_CONNECTION_FAILED("데이터베이스 연결에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_ERROR("미등록 에러가 발생했습니다.",HttpStatus.INTERNAL_SERVER_ERROR)

    ;

    private final String message;
    private HttpStatus httpStatus;
}
