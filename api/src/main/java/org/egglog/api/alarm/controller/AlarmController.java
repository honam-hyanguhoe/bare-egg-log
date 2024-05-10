package org.egglog.api.alarm.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.egglog.api.alarm.model.dto.params.AlarmForm;
import org.egglog.api.alarm.model.dto.params.AlarmModifyForm;
import org.egglog.api.alarm.model.dto.params.AlarmStatusModifyForm;
import org.egglog.api.alarm.model.service.AlarmService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.alarm.controller
 * fileName      : AlarmController
 * description    : 알람 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-24|김도휘|최초 생성|
 * |2024-05-10|김형민|1차 리펙토링|
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    /**
     * 알람 리스트 조회
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getAlarmList(
            @AuthenticationPrincipal User loginUser
    ) {
        return ResponseEntity.ok().body(MessageUtils.success(alarmService.getAlarmList(loginUser)));
    }


    @PostMapping("")
    public ResponseEntity<MessageUtils> registerAlarm(
            @RequestBody @Valid AlarmForm alarmForm,
            @AuthenticationPrincipal User loginUser)
    {
        alarmService.registerAlarm(alarmForm, loginUser);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.NO_CONTENT));
    }

    /**
     * 알람 상태 변경
     *
     * @param alarmStatusModifyForm
     * @return
     */
    @PatchMapping("/status")
    public ResponseEntity<?> modifyAlarmStatus(
            @RequestBody AlarmStatusModifyForm alarmStatusModifyForm,
            @AuthenticationPrincipal User loginUser
    ) {
        return ResponseEntity.ok().body(MessageUtils.success(alarmService.modifyAlarmStatus(alarmStatusModifyForm, loginUser)));
    }


    /**
     * 알람 정보 변경
     *
     * @param alarmModifyForm
     * @return
     */
    @PatchMapping("")
    public ResponseEntity<?> modifyAlarm(
            @RequestBody @Valid AlarmModifyForm alarmModifyForm,
            @AuthenticationPrincipal User loginUser
    )
    {
        return ResponseEntity.ok().body(MessageUtils.success(alarmService.modifyAlarm(alarmModifyForm, loginUser)));
    }

    /**
     * 알람 삭제
     *
     * @param alarmId
     * @return
     */
    @DeleteMapping("/{alarm_id}")
    public ResponseEntity<?> deleteAlarm(
            @PathVariable("alarm_id") Long alarmId,
            @AuthenticationPrincipal User loginUser
    ) {
        alarmService.deleteAlarm(alarmId, loginUser);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

}
