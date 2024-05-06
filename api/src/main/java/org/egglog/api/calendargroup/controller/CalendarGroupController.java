package org.egglog.api.calendargroup.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendargroup.model.dto.params.CalendarGroupListRequest;
import org.egglog.api.calendargroup.model.dto.request.CreateCalGroupRequest;
import org.egglog.api.calendargroup.model.service.CalendarGroupService;
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

    /**
     * 캘린더 그룹 생성 API
     * @param loginUser
     * @param request 그룹 별칭, url(없다면 안보내도 됨)
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<MessageUtils> registerCalendarGroup(
            @AuthenticationPrincipal User loginUser,
            @RequestBody CreateCalGroupRequest request
        ) {
        return ResponseEntity.ok()
                .body(MessageUtils.success(calendarGroupService.registerCalendarGroup(request, loginUser)));
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEventListByCalendarGroupIds(@ModelAttribute CalendarGroupListRequest calendarGroupListRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(calendarGroupService.getEventListByCalenderIds(calendarGroupListRequest, user)));
    }

}
