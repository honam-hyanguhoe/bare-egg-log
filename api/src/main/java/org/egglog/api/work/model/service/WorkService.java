package org.egglog.api.work.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.work.model.dto.request.CreateAndEditWorkListRequest;
import org.egglog.api.work.repository.jpa.WorkJpaRepository;
import org.egglog.api.work.repository.jpa.WorkQueryRepository;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.jpa.WorkTypeJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;


/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.service
 * fileName      : WorkService
 * description    : 근무 일정 서비스
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-29|김형민|최초 생성|
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkService {

    private final WorkJpaRepository workJpaRepository;
    private final WorkQueryRepository workQueryRepository;
    private final CalendarGroupRepository calendarGroupRepository;
    private final WorkTypeJpaRepository workTypeJpaRepository;

    @Transactional
    public void createWork(User user, CreateAndEditWorkListRequest request){
        CalendarGroup calendarGroup = calendarGroupRepository.findById(request.getCalendarGroupId())
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));
        Map<Long, WorkType> userWorkTypeMap = workTypeJpaRepository
                .findByUser(user).stream().collect(Collectors.toMap(WorkType::getId, wt -> wt));
        workJpaRepository.saveAll(
                request.getWorkTypes().stream()
                        .map(value-> value.toEntity(user, userWorkTypeMap, calendarGroup))
                        .collect(Collectors.toList()));
    }


}
