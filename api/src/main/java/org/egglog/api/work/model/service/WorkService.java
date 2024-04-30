package org.egglog.api.work.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.work.exception.WorkErrorCode;
import org.egglog.api.work.exception.WorkException;
import org.egglog.api.work.model.dto.request.CreateWorkListRequest;
import org.egglog.api.work.model.dto.request.EditAndDeleteWorkListRequest;
import org.egglog.api.work.model.dto.request.EditAndDeleteWorkRequest;
import org.egglog.api.work.model.dto.request.FindWorkListRequest;
import org.egglog.api.work.model.dto.response.WorkListResponse;
import org.egglog.api.work.model.dto.response.WorkResponse;
import org.egglog.api.work.model.entity.Work;
import org.egglog.api.work.repository.jpa.WorkJpaRepository;
import org.egglog.api.work.repository.jpa.WorkQueryRepository;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.jpa.WorkTypeJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    public void createWork(User loginUser, CreateWorkListRequest request){
        CalendarGroup calendarGroup = calendarGroupRepository.findById(request.getCalendarGroupId())
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));
        if (calendarGroup.getUser().getId()!=loginUser.getId()) throw new WorkException(WorkErrorCode.ACCESS_DENIED);
        Map<Long, WorkType> userWorkTypeMap = workTypeJpaRepository
                .findByUser(loginUser).stream().collect(Collectors.toMap(WorkType::getId, wt -> wt));
        workJpaRepository.saveAll(
                request.getWorkTypes().stream()
                        .map(value-> value.toEntity(loginUser, userWorkTypeMap, calendarGroup))
                        .collect(Collectors.toList()));
    }

    @Transactional
    public void updateWork(User loginUser, EditAndDeleteWorkListRequest request){
        CalendarGroup calendarGroup = calendarGroupRepository.findCalendarGroupWithUserById(request.getCalendarGroupId())
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));
        if (calendarGroup.getUser().getId()!=loginUser.getId()) throw new WorkException(WorkErrorCode.ACCESS_DENIED);
        Map<Long, WorkType> userWorkTypeMap = workTypeJpaRepository
                .findByUser(loginUser).stream().collect(Collectors.toMap(WorkType::getId, wt -> wt));
        //EditAndDeleteWorkListRequest 내에 있는 EditAndDeleteWorkRequest의 isDeleted이 true 이면 삭제, 그게 아니라면 새 데이터 저장
        workJpaRepository.saveAll(request.getEditWorkList().stream().flatMap(editRequest -> {
            if (editRequest.getIsDeleted()) {
                workJpaRepository.deleteById(editRequest.getWorkId());
                return Stream.empty(); // 삭제는 여기서 처리하고, 스트림에서는 빈 결과를 반환
            } else {
                return workJpaRepository.findById(editRequest.getWorkId())
                        .map(work -> {
                            work.setWorkDate(editRequest.getWorkDate());
                            work.setWorkType(userWorkTypeMap.get(editRequest.getWorkTypeId()));
                            return work;
                        })
                        .stream(); //유효한 업데이트가 있는 경우에만 스트림에 추가
            }
        }).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public WorkListResponse findWorkList(User loginUser, FindWorkListRequest request){
        CalendarGroup calendarGroup = calendarGroupRepository.findCalendarGroupWithUserById(request.getCalendarGroupId())
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));
        if (calendarGroup.getUser().getId()!=loginUser.getId()) throw new WorkException(WorkErrorCode.ACCESS_DENIED);
        List<WorkResponse> workResponses = workQueryRepository.findWorkListAllByTime(request.getCalendarGroupId(), request.getStartDate(), request.getEndDate())
                .stream()
                .map(Work::toResponse)
                .collect(Collectors.toList());
        return WorkListResponse.builder()
                .calendarGroup(calendarGroup.toResponse())
                .workList(workResponses)
                .build();
    }




}
