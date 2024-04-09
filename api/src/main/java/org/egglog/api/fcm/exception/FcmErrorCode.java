package org.egglog.api.fcm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FcmErrorCode {
    SUBSCRIBE_FAIL("토픽 구독에 실패했습니다.", HttpStatus.BAD_REQUEST),
    CAN_NOT_SEND_NOTIFICATION("푸시 알림 전송에 실패했습니다.", HttpStatus.BAD_REQUEST),
    NO_EXIST_TARGET("푸시 알림 전송에 실패했습니다.", HttpStatus.BAD_REQUEST),
    NO_EXIST_TOKEN("본인 디바이스 토큰이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private HttpStatus httpStatus;
}
