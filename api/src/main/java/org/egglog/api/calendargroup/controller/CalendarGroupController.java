package org.egglog.api.calendargroup.controller;

import jakarta.validation.Valid;
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
/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.calendargroup.controller
 * fileName       : CalendarGroupController
 * description    : 캘린더 그룹 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-20|김도휘|최초 생성|
 * |2024-04-20|김도휘|캘린더 그룹으로 개인 일정 조회 추가|
 * |2024-05-07|김형민|리스트조회, 생성, 삭제 추가|
 * |2024-05-08|김형민|1차 리펙토링|
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/calendargroups")
public class CalendarGroupController {

    private final CalendarGroupService calendarGroupService;

    /**
     * 캘린더 그룹 생성
     * @param loginUser
     * @param request 그룹 별칭, url(없다면 안보내도 됨)
     * @return
     * @author 김형민
     */
    @PostMapping("/create")
    public ResponseEntity<MessageUtils> registerCalendarGroup(
            @AuthenticationPrincipal User loginUser,
            @RequestBody @Valid CreateCalGroupRequest request
        ) {
        return ResponseEntity.ok()
                .body(MessageUtils.success(calendarGroupService.registerCalendarGroup(request, loginUser)));
    }

    /**
     * 캘린더 그룹 삭제
     * @param loginUser 현재 로그인한 유저 JWT
     * @param calendarGroupId 삭제할 calendarGroupId
     * @return
     * @author 김형민
     */
    @DeleteMapping("/delete/{calendarGroupId}")
    public ResponseEntity<MessageUtils> deleteCalendarGroup(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Long calendarGroupId
    ){
        calendarGroupService.deleteCalendarGroup(calendarGroupId, loginUser);
        return ResponseEntity.ok()
                .body(MessageUtils.success(SuccessType.DELETE));
    }

    /**
     * 캘린더 그룹 리스트 조회
     * @param loginUser 현재 로그인한 유저 JWT
     * @return
     * @author 김형민
     */
    @GetMapping("/find/list")
    public ResponseEntity<MessageUtils> findList(
            @AuthenticationPrincipal User loginUser
    ){
        return ResponseEntity.ok()
                .body(MessageUtils.success(calendarGroupService.findCalendarGroupList(loginUser)));
    }

    /**
     * 캘린더 그룹 삭제
     * @param request 일정을 조회할 calendarGroupID 리스트(다중선택)
     * @param user 현재 로그인한 유저 JWT
     * @return
     * @author 김도휘
     */
    @GetMapping("/events")
    public ResponseEntity<?> getEventListByCalendarGroupIds(
            @ModelAttribute CalendarGroupListRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok()
                .body(MessageUtils.success(calendarGroupService.getEventListByCalenderIds(request, user)));
    }

}
