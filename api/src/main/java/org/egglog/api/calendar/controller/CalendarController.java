package org.egglog.api.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendar.model.service.CalendarService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/calendar")
public class CalendarController {

    private final CalendarService calendarService;

//    @PostMapping("")
//    public ResponseEntity<?> registerSchedule(@RequestBody ScheduleForm scheduleForm) {
////        TODO @AuthenticationPrincipal User user
//        Long userId = 1L;
//        calendarService.
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
}
