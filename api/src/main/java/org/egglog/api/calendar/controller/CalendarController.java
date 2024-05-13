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

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
 * |2024-05-13|김도휘|캘린더 한달 조회 api
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
     * 생성
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param request 이름, 색상, 근무 이미지, 시작시각, 근무시간
     * @return 근무타입ID, 이름, 색상, 근무 이미지, 태그 속성, 시작시각, 근무시간
     * @author 김형민
     */

    /**
     * 캘린더 startDate - endDate 개인 일정 & 근무 일정 조회
     * @param calendarMonthRequest 시작날짜, 종료날짜, 캘린더 그룹 ID
     * @param user
     * @return
     */
    @GetMapping("/month")
    public ResponseEntity<MessageUtils<List<CalendarListResponse>>> getCalendarByMonth(@ModelAttribute @Valid CalendarMonthRequest calendarMonthRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(calendarService.getCalendarList(calendarMonthRequest, user)));
    }

}
