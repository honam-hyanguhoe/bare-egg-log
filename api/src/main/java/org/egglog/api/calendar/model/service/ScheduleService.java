package org.egglog.api.calendar.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendar.dto.params.ScheduleForm;
import org.egglog.api.calendar.dto.response.ScheduleListOutputSpec;
import org.egglog.api.calendar.model.entity.Schedule;
import org.egglog.api.calendar.model.repository.ScheduleRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserJpaRepository userJpaRepository;

    public void registerSchedule(ScheduleForm scheduleForm, Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        Schedule schedule = Schedule.builder()
                .scheduleTitle(scheduleForm.getScheduleTitle())
                .scheduleContent(scheduleForm.getScheduleContent())
                .startDate(scheduleForm.getStartDate())
                .endDate(scheduleForm.getEndDate())
                .user(user)
                .build();

        scheduleRepository.save(schedule);

    }

    public List<ScheduleListOutputSpec> getScheduleList(Long userId) {
        List<Schedule> scheduleList = scheduleRepository.findAllByUserId(userId);
        List<ScheduleListOutputSpec> scheduleListOutputSpecList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            ScheduleListOutputSpec scheduleListOutputSpec = ScheduleListOutputSpec.builder()
                    .scheduleId(schedule.getId())
                    .scheduleTitle(schedule.getScheduleTitle())
                    .scheduleContent(schedule.getScheduleContent())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .build();

            scheduleListOutputSpecList.add(scheduleListOutputSpec);
        }
        return scheduleListOutputSpecList;
    }


}
