package org.egglog.api.calendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.calendar.model.dto.params.CalendarMonthRequest;
import org.egglog.api.calendar.model.service.CalendarService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Calendar;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/calendar")
@Slf4j
public class CalendarController {
    @Value("${egg-log.test.value}")
    private String testVal;

    private final CalendarService calendarService;

    @GetMapping("/test")
    public ResponseEntity test(){
        log.warn(testVal);
        return ResponseEntity.ok().body(MessageUtils.success(testVal));
    }

    @GetMapping("/link")
    public ResponseEntity<?> getCalendarUrl(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(calendarService.getIcsLink(user)));
    }

    @GetMapping("/month")
    public ResponseEntity<?> getCalendarByMonth(@ModelAttribute CalendarMonthRequest calendarMonthRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(calendarService.getCalendarListByMonth(calendarMonthRequest, user)));
    }

}
