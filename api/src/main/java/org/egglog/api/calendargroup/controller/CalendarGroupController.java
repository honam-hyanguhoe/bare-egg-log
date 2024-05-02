package org.egglog.api.calendargroup.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendargroup.model.dto.params.CalendarGroupForm;
import org.egglog.api.calendargroup.model.service.CalendarGroupService;
import org.egglog.api.event.model.dto.params.EventForm;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
