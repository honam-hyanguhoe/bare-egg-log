package org.egglog.api.event.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.event.model.dto.params.EventForm;
import org.egglog.api.event.model.dto.params.EventUpdateForm;
import org.egglog.api.event.model.service.EventService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> registerEvent(@RequestBody EventForm eventForm, @AuthenticationPrincipal User user) {
        eventService.registerEvent(eventForm, user.getId());
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    /**
     * 일정 상세조회
     *
     * @param eventId
     * @param user
     * @return
     */
    @GetMapping("/{event_id}")
    public ResponseEntity<?> getEvent(@PathVariable("event_id") Long eventId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(eventService.getEvent(eventId, user.getId())));
    }

    /**
     * 일정 목록 조회
     * @param user
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getEventList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(eventService.getEventList(user.getId())));
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
    public ResponseEntity<?> modifyEvent(@PathVariable("event_id") Long eventId,
                                         @RequestBody EventUpdateForm eventUpdateForm, @AuthenticationPrincipal User user) {
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
    public ResponseEntity deleteEvent(
            @PathVariable("event_id") Long eventId, @AuthenticationPrincipal User user) {
        eventService.deleteEvent(eventId, user.getId());
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }
}
