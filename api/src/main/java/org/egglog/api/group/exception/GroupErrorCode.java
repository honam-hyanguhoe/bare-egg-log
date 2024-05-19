package org.egglog.api.group.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupErrorCode implements ErrorFormat {
    NOT_FOUND("존재하지않는 데이터입니다.",HttpStatus.BAD_REQUEST),
    DATABASE_CONNECTION_FAILED("데이터베이스 연결에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    TRANSACTION_ERROR("트랜잭션에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    GROUP_ROLE_NOT_MATCH("권한이 없는 사용자의 요청입니다.", HttpStatus.NOT_ACCEPTABLE),
    DUPLICATED_MEMBER("이미 존재하는 사용자입니다.", HttpStatus.BAD_REQUEST),
    UNKNOWN_ERROR("미등록 에러가 발생했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    EXPIRED_INVITATION("만료된 초대입니다.",HttpStatus.BAD_REQUEST),
    NOT_MATCH_INVITATION("올바르지 않은 초대입니다.",HttpStatus.BAD_REQUEST),
    NOT_FOUND_INVITATION("존재하지않는 초대입니다.",HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
