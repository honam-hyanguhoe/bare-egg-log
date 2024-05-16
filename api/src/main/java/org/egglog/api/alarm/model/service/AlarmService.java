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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;

    private final UserJpaRepository userJpaRepository;

    private final WorkTypeJpaRepository workTypeJpaRepository;

    @Transactional(readOnly = true)
    public List<AlarmListOutputSpec> getAlarmList(User loginUser) {
        List<AlarmListOutputSpec> alarmListOutputSpecList = new ArrayList<>();
        try {
            Optional<List<Alarm>> alarmList = alarmRepository.findAlarmListByUserId(loginUser.getId());
            if (alarmList.isPresent()) {
                for (Alarm alarm : alarmList.get()) {
                    WorkType workType = alarm.getWorkType();
                    AlarmListOutputSpec alarmListOutputSpec = AlarmListOutputSpec.builder()
                            .alarmId(alarm.getId())
                            .alarmTime(alarm.getAlarmTime())
                            .replayCnt(alarm.getReplayCnt())
                            .replayTime(alarm.getReplayTime())
                            .isAlarmOn(alarm.getIsAlarmOn())
                            .workTypeId(workType.getId())
                            .workTypeTitle(workType.getTitle())
                            .workTypeColor(workType.getColor())
                            .build();

                    alarmListOutputSpecList.add(alarmListOutputSpec);
                }
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
     * @param loginUser
     * @return
     */
    @Transactional
    public AlarmOutputSpec modifyAlarmStatus(AlarmStatusModifyForm alarmStatusModifyForm, User loginUser) {
        Alarm alarm = alarmRepository.findWithUserAndWorkTypeById(alarmStatusModifyForm.getAlarmId()).orElseThrow(
                () -> new AlarmException(AlarmErrorCode.NO_EXIST_ALARM)
        );
        WorkType workType = alarm.getWorkType();
        if (!alarm.getUser().equals(loginUser)) throw new UserException(UserErrorCode.ACCESS_DENIED);

        alarm.setIsAlarmOn(alarmStatusModifyForm.getIsAlarmOn());
        return alarm.toOutputSpec(workType.getTitle());
    }

    public void registerAlarm(AlarmForm alarmForm, User loginUser) {
        WorkType workType = workTypeJpaRepository.findWithUserById(alarmForm.getWorkTypeId()).orElseThrow(
                () -> new WorkTypeException(WorkTypeErrorCode.NO_EXIST_WORKTYPE)
        );
        if (!workType.getUser().equals(loginUser)) throw new UserException(UserErrorCode.ACCESS_DENIED);
        Alarm alarm = Alarm.builder()
                .alarmTime(alarmForm.getAlarmTime())
                .replayCnt(alarmForm.getReplayCnt())
                .replayTime(alarmForm.getReplayTime())
                .isAlarmOn(false)
                .workType(workType)
                .user(loginUser)
                .build();

        alarmRepository.save(alarm);
    }

    @Transactional
    public AlarmOutputSpec modifyAlarm(AlarmModifyForm alarmModifyForm, User loginUser) {
        WorkType editWorkType = workTypeJpaRepository.findById(alarmModifyForm.getWorkTypeId()).orElseThrow(() -> new WorkTypeException(WorkTypeErrorCode.NO_EXIST_WORKTYPE));
        Alarm alarm = alarmRepository.findWithUserById(alarmModifyForm.getAlarmId()).orElseThrow(() -> new AlarmException(AlarmErrorCode.NO_EXIST_ALARM));

        if (!alarm.getUser().equals(loginUser)) throw new UserException(UserErrorCode.ACCESS_DENIED);

        return alarmRepository.save(
                alarm.modify(alarmModifyForm.getAlarmTime(), alarmModifyForm.getReplayCnt(), alarmModifyForm.getReplayTime(), editWorkType))
                .toOutputSpec(editWorkType.getTitle());
    }

    /**
     * 알람 삭제
     *
     * @param alarmId
     */
    @Transactional
    public void deleteAlarm(Long alarmId, User loginUser) {
        Alarm alarm = alarmRepository.findWithUserById(alarmId).orElseThrow(
                () -> new AlarmException(AlarmErrorCode.NO_EXIST_ALARM));
        if (!alarm.getUser().equals(loginUser)) throw new UserException(UserErrorCode.ACCESS_DENIED);

        alarmRepository.delete(alarm);
    }

}
