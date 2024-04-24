package org.egglog.api.alarm.controller;


import lombok.RequiredArgsConstructor;
import org.egglog.api.alarm.model.dto.params.AlarmForm;
import org.egglog.api.alarm.model.dto.params.AlarmModifyForm;
import org.egglog.api.alarm.model.dto.params.AlarmStatusModifyForm;
import org.egglog.api.alarm.model.service.AlarmService;
import org.egglog.api.board.model.dto.params.BoardListForm;
import org.egglog.api.board.model.dto.params.BoardModifyForm;
import org.egglog.api.group.model.dto.request.InvitationAcceptForm;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getAlarmList() {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(alarmService.getAlarmList(userId)));
    }


    @PostMapping("")
    public ResponseEntity registerAlarm(@RequestBody AlarmForm alarmForm
//            TODO @AuthenticationPrincipal User user){
    ) {
        Long userId = null;
        alarmService.registerAlarm(alarmForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.NO_CONTENT));
    }

    /**
     * 알람 상태 변경
     *
     * @param alarmStatusModifyForm
     * @return
     */
    @PatchMapping("/status")
    public ResponseEntity<?> modifyAlarmStatus(@RequestBody AlarmStatusModifyForm alarmStatusModifyForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(alarmService.modifyAlarmStatus(alarmStatusModifyForm, userId)));
    }

    /**
     * 알람 정보 변경
     *
     * @param alarmModifyForm
     * @return
     */
    @PatchMapping("")
    public ResponseEntity<?> modifyAlarm(@RequestBody AlarmModifyForm alarmModifyForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(alarmService.modifyAlarm(alarmModifyForm, userId)));
    }

    /**
     * 알람 삭제
     *
     * @param alarmId
     * @return
     */
    @DeleteMapping("/{alarm_id}")
    public ResponseEntity<?> deleteAlarm(@PathVariable("alarm_id") Long alarmId) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        alarmService.deleteAlarm(alarmId);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

}
