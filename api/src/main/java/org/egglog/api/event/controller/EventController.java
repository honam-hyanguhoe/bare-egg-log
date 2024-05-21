package org.egglog.api.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.egglog.api.event.model.dto.params.EventForm;
import org.egglog.api.event.model.dto.params.EventPeriodRequest;
import org.egglog.api.event.model.dto.params.EventUpdateForm;
import org.egglog.api.event.model.dto.response.EventListOutputSpec;
import org.egglog.api.event.model.dto.response.EventListPeriodSpec;
import org.egglog.api.event.model.dto.response.EventOutputSpec;
import org.egglog.api.event.model.service.EventService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.event.controller
 * fileName      : EventController
 * description    : 개인 일정을 관리하는 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-24|김도휘|최초 생성|
 * |2024-05-10|김형민|1차 리펙토링|
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class EventController {

    private final EventService eventService;

    /**
     * 일정 등록
     *
     * @param eventForm
     * @param user
     * @return
     */
    @PostMapping("")
    public ResponseEntity<MessageUtils> registerEvent(@RequestBody @Valid EventForm eventForm, @AuthenticationPrincipal User user) {
        eventService.registerEvent(eventForm, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    /**
     * 일정 상세조회
     *
     * @param eventId
     * @param user
     * @return
     */
    @GetMapping("/{event_id}")
    public ResponseEntity<MessageUtils<EventOutputSpec>> getEvent(@PathVariable("event_id") Long eventId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(eventService.getEvent(eventId, user.getId())));
    }

    /**
     * 사용자의 모든 일정 목록 조회
     * @param user
     * @return
     */
    @GetMapping("")
    public ResponseEntity<MessageUtils<List<EventListOutputSpec>>> getEventList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(eventService.getEventList(user.getId())));
    }
//    getCalendarListByMonth

    /**
     * 캘린더그룹의 기간 내 개인 일정 조회
     * @param eventPeriodRequest
     * @param user
     * @return
     */
    @GetMapping("/month")
    public ResponseEntity<MessageUtils<List<EventListPeriodSpec>>> getEventListByPeriod(@ModelAttribute @Valid EventPeriodRequest eventPeriodRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(eventService.getEventListByPeriod(eventPeriodRequest, user)));
    }

    /**
     * 일정 수정
     *
     * @param eventId
     * @param eventUpdateForm
     * @param user
     * @return
     */
    @PatchMapping("/{event_id}")
    public ResponseEntity<MessageUtils<EventOutputSpec>> modifyEvent(@PathVariable("event_id") Long eventId,
                                         @RequestBody @Valid EventUpdateForm eventUpdateForm, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(eventService.modifyEvent(eventId, eventUpdateForm, user.getId())));
    }

    /**
     * 일정 삭제
     *
     * @param eventId
     * @param user
     * @return
     */
    @DeleteMapping("/{event_id}")
    public ResponseEntity<MessageUtils> deleteEvent(
            @PathVariable("event_id") Long eventId, @AuthenticationPrincipal User user) {
        eventService.deleteEvent(eventId, user.getId());
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }
}
