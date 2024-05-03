package org.egglog.api.calendargroup.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendargroup.model.dto.params.CalendarGroupForm;
import org.egglog.api.calendargroup.model.dto.params.CalendarGroupListRequest;
import org.egglog.api.calendargroup.model.service.CalendarGroupService;
import org.egglog.api.event.model.dto.params.EventForm;
import org.egglog.api.event.model.dto.params.EventPeriodRequest;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/calendargroups")
public class CalendarGroupController {

    private final CalendarGroupService calendarGroupService;

    @PostMapping("")
    public ResponseEntity<?> registerCalendarGroup(@RequestBody CalendarGroupForm calendarGroupForm, @AuthenticationPrincipal User user) {
        calendarGroupService.registerCalendarGroup(calendarGroupForm, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEventListByCalendarGroupIds(@ModelAttribute CalendarGroupListRequest calendarGroupListRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(calendarGroupService.getEventListByCalenderIds(calendarGroupListRequest, user)));
    }

}
