package org.egglog.api.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendar.model.service.CalendarService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/{groupId}")
    public ResponseEntity getCalendarUrl(@AuthenticationPrincipal User user){
        Long groupId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(calendarService.getIcsLink(user,groupId)));
    }
//    @PostMapping("")
//    public ResponseEntity<?> registerSchedule(@RequestBody ScheduleForm scheduleForm) {
////        TODO @AuthenticationPrincipal User user
//        Long userId = 1L;
//        calendarService.
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
}
