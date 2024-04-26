package org.egglog.api.inquiry.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum InquiryErrorCode implements ErrorFormat {
    NOT_FOUND("존재하지않는 데이터입니다.",HttpStatus.BAD_REQUEST),
    DATABASE_CONNECTION_FAILED("데이터베이스 연결에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    TRANSACTION_ERROR("트랜잭션에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    INQUIRY_ROLE_NOT_MATCH("권한이 없는 사용자의 요청입니다.", HttpStatus.UNAUTHORIZED),
    UNKNOWN_ERROR("미등록 에러가 발생했습니다.",HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}
