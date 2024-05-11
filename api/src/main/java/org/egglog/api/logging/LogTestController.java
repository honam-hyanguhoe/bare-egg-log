package org.egglog.api.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.logging
 * fileName      : LogTestController
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-10|김다희|최초 생성|
 */
@RestController
@RequestMapping("/v1/logger")
@RequiredArgsConstructor
@Slf4j
public class LogTestController {
    @GetMapping("/test")
    public ResponseEntity makeLog(){
        log.trace("trace....... : 모든 레벨에 대한 로깅이 추적됩니다. ");
        log.debug("디버그 로그 발생 : 개발 단계, SQL 로깅이 가능합니다.");
        log.info("인포 로그 발생 : 운영에 참고할만한 사항, 중요한 비즈니스 프로세스가 완료되었습니다.");
        log.warn("warn 로그 발생 : 로직상 유효성이 확인되었으며, 예상 가능한 문제일 경우 표시합니다.");
        log.error("error 로그 발생:error는 예상하지 못한 심각한 문제가 발생할 경우 표시합니다.");
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.NO_CONTENT));
    }
}
