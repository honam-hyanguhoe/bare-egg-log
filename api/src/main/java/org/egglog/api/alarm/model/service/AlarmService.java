package org.egglog.api.alarm.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.alarm.exception.AlarmErrorCode;
import org.egglog.api.alarm.exception.AlarmException;
import org.egglog.api.alarm.model.dto.params.AlarmForm;
import org.egglog.api.alarm.model.dto.params.AlarmModifyForm;
import org.egglog.api.alarm.model.dto.params.AlarmStatusModifyForm;
import org.egglog.api.alarm.model.dto.response.AlarmListOutputSpec;
import org.egglog.api.alarm.model.dto.response.AlarmOutputSpec;
import org.egglog.api.alarm.model.entity.Alarm;
import org.egglog.api.alarm.repository.jpa.AlarmRepository;
import org.egglog.api.board.exception.BoardErrorCode;
import org.egglog.api.board.exception.BoardException;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.egglog.api.user.repository.jpa.UserQueryRepositoryImpl;
import org.egglog.api.worktype.exception.WorkTypeErrorCode;
import org.egglog.api.worktype.exception.WorkTypeException;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.jpa.WorkTypeJpaRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;

    private final UserJpaRepository userJpaRepository;

    private final WorkTypeJpaRepository workTypeJpaRepository;

    public List<AlarmListOutputSpec> getAlarmList(Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        List<AlarmListOutputSpec> alarmListOutputSpecList = new ArrayList<>();

        try {
            List<Alarm> alarmList = alarmRepository.findAlarmList(userId);

            for (Alarm alarm : alarmList) {
                AlarmListOutputSpec alarmListOutputSpec = AlarmListOutputSpec.builder()
                        .alarmId(alarm.getId())
                        .alarmTime(alarm.getAlarmTime())
                        .replayCnt(alarm.getReplayCnt())
                        .replayTime(alarm.getReplayTime())
                        .isAlarmOn(alarm.getIsAlarmOn())
                        .build();

                alarmListOutputSpecList.add(alarmListOutputSpec);
            }

        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }

        return alarmListOutputSpecList;
    }

    /**
     * 알람 On / Off
     *
     * @param alarmStatusModifyForm
     * @param userId
     * @return
     */
    @Transactional
    public AlarmOutputSpec modifyAlarmStatus(AlarmStatusModifyForm alarmStatusModifyForm, Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Alarm alarm = alarmRepository.findById(alarmStatusModifyForm.getAlarmId()).orElseThrow(
                () -> new AlarmException(AlarmErrorCode.NO_EXIST_ALARM)
        );
        WorkType workType = workTypeJpaRepository.findById(alarm.getWorkType().getId()).orElseThrow(
                () -> new WorkTypeException(WorkTypeErrorCode.NO_EXIST_WORKTYPE)
        );

        alarm.setIsAlarmOn(alarmStatusModifyForm.getIsAlarmOn());

        AlarmOutputSpec alarmOutputSpec = AlarmOutputSpec.builder()
                .alarmId(alarmStatusModifyForm.getAlarmId())
                .alarmTime(alarm.getAlarmTime())
                .alarmReplayCnt(alarm.getReplayCnt())
                .alarmReplayTime(alarm.getReplayTime())
                .isAlarmOn(alarmStatusModifyForm.getIsAlarmOn())
                .workTypeTitle(workType.getTitle())
                .build();

        return alarmOutputSpec;
    }

    public void registerAlarm(AlarmForm alarmForm, Long userId) {
        WorkType workType = workTypeJpaRepository.findById(alarmForm.getWorkTypeId()).orElseThrow(
                () -> new WorkTypeException(WorkTypeErrorCode.NO_EXIST_WORKTYPE)
        );
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        Alarm alarm = Alarm.builder()
                .alarmTime(alarmForm.getAlarmTime())
                .replayCnt(alarmForm.getReplayCnt())
                .replayTime(alarmForm.getReplayTime())
                .isAlarmOn(false)
                .workType(workType)
                .user(user)
                .build();

        alarmRepository.save(alarm);
    }

    @Transactional
    public AlarmOutputSpec modifyAlarm(AlarmModifyForm alarmModifyForm, Long userId) {
        WorkType workType = workTypeJpaRepository.findById(alarmModifyForm.getWorkTypeId()).orElseThrow(
                () -> new WorkTypeException(WorkTypeErrorCode.NO_EXIST_WORKTYPE)
        );
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Alarm alarm = alarmRepository.findById(alarmModifyForm.getAlarmId()).orElseThrow(
                () -> new AlarmException(AlarmErrorCode.NO_EXIST_ALARM)
        );

        alarm.setAlarmTime(alarmModifyForm.getAlarmTime());
        alarm.setReplayCnt(alarmModifyForm.getReplayCnt());
        alarm.setReplayTime(alarmModifyForm.getReplayTime());

        AlarmOutputSpec alarmOutputSpec = AlarmOutputSpec.builder()
                .alarmId(alarmModifyForm.getAlarmId())
                .alarmTime(alarmModifyForm.getAlarmTime())
                .alarmReplayCnt(alarmModifyForm.getReplayCnt())
                .alarmReplayTime(alarmModifyForm.getReplayTime())
                .isAlarmOn(alarm.getIsAlarmOn())
                .workTypeTitle(workType.getTitle())
                .build();

        return alarmOutputSpec;
    }

    /**
     * 알람 삭제
     *
     * @param alarmId
     */
    public void deleteAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
                () -> new AlarmException(AlarmErrorCode.NO_EXIST_ALARM)
        );

        try {
            alarmRepository.delete(alarm);

        } catch (Exception e) {
            throw new AlarmException(AlarmErrorCode.TRANSACTION_ERROR);
        }
    }

}
