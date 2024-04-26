package org.egglog.api.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendar.model.dto.params.EventForm;
import org.egglog.api.calendar.model.dto.params.EventUpdateForm;
import org.egglog.api.calendar.model.service.EventService;
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

    @PostMapping("")
    public ResponseEntity<?> registerEvent(@RequestBody EventForm eventForm, @AuthenticationPrincipal User user) {
        eventService.registerEvent(eventForm, user.getId());
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @GetMapping("/{event_id}")
    public ResponseEntity<?> getEvent(@PathVariable("event_id") Long eventId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(eventService.getEvent(eventId, user.getId())));
    }

    @GetMapping("")
    public ResponseEntity<?> getEventList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(eventService.getEventList(user.getId())));
    }

    @PatchMapping("/{event_id}")
    public ResponseEntity<?> modifyEvent(@PathVariable("event_id") Long eventId,
                                         @RequestBody EventUpdateForm eventUpdateForm, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(eventService.modifyEvent(eventId, eventUpdateForm, user.getId())));
    }

    @DeleteMapping("/{event_id}")
    public ResponseEntity deleteEvent(
            @PathVariable("event_id") Long eventId, @AuthenticationPrincipal User user) {
        eventService.deleteEvent(eventId, user.getId());
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }
}
