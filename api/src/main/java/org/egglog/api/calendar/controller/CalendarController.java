package org.egglog.api.calendar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.calendar.model.dto.params.CalendarMonthRequest;
import org.egglog.api.calendar.model.dto.response.CalendarListResponse;
import org.egglog.api.calendar.model.service.CalendarService;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Calendar;
/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.calendar.controller
 * fileName      : CalendarController
 * description    : 캘린더 동기화 관련 api
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-24|김다희|최초 생성|
 * |2024-05-10|김형민|동기화 api 추가|
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/calendar")
@Slf4j
public class CalendarController {
    @Value("${egg-log.test.value}")
    private String testVal;

    private final CalendarService calendarService;

    /**
     * 테스트용 사용 x
     * @return
     */
    @GetMapping("/test")
    public ResponseEntity<MessageUtils> test(){
        log.warn(testVal);
        return ResponseEntity.ok().body(MessageUtils.success(testVal));
    }

    /**
     * 캘린더 동기화
     * @return
     */
    @PostMapping("/sync")
    public ResponseEntity<MessageUtils> syncCalendar(
            @AuthenticationPrincipal User loginUSer
    ){
        calendarService.updateIcsEventsForSyncRequest(loginUSer);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    /**
     * 캘린더 동기화 링크 생성 (외부 캘린더랑 연동용 URL 생성)
     * @return
     */
    @GetMapping("/link")
    public ResponseEntity<MessageUtils<String>> getCalendarUrl(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(calendarService.getIcsLink(user)));
    }

    /**
     * ???
     * @return
     */
    @GetMapping("/month")
    public ResponseEntity<MessageUtils<CalendarListResponse>> getCalendarByMonth(@ModelAttribute @Valid CalendarMonthRequest calendarMonthRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(calendarService.getCalendarListByMonth(calendarMonthRequest, user)));
    }

}
