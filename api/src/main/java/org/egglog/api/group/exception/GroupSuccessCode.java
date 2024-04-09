package org.egglog.api.group.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupSuccessCode {
    NO_CONTENT("응답이 없는 요청입니다.",HttpStatus.NO_CONTENT),
    SUCCESS("요청을 성공적으로 수행완료했습니다.",HttpStatus.OK);

    private final String message;
    private final HttpStatus httpStatus;
}
