package org.egglog.api.work.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.repository.jpa.GroupMemberRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.egglog.api.work.exception.WorkErrorCode;
import org.egglog.api.work.exception.WorkException;
import org.egglog.api.work.model.dto.request.*;
import org.egglog.api.work.model.dto.response.completed.CompletedWorkCountWeekResponse;
import org.egglog.api.work.model.dto.response.upcoming.UpComingCountWorkResponse;
import org.egglog.api.work.model.dto.response.WorkListResponse;
import org.egglog.api.work.model.dto.response.WorkResponse;
import org.egglog.api.work.model.dto.response.completed.CompletedWorkCountResponse;
import org.egglog.api.work.model.dto.response.upcoming.enums.DateType;
import org.egglog.api.work.model.entity.Work;
import org.egglog.api.work.repository.jpa.WorkJpaRepository;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.jpa.WorkTypeJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
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
    private final CalendarGroupRepository calendarGroupRepository;
    private final WorkTypeJpaRepository workTypeJpaRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserJpaRepository userJpaRepository;

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
        List<WorkResponse> workResponses = workJpaRepository.findWorkListWithWorkTypeByTime(request.getCalendarGroupId(), request.getStartDate(), request.getEndDate())
                .stream()
                .map(Work::toResponse)
                .collect(Collectors.toList());
        return WorkListResponse.builder()
                .calendarGroup(calendarGroup.toResponse())
                .workList(workResponses)
                .build();
    }

    @Transactional(readOnly = true)
    public List<WorkResponse> findGroupUserWorkList(User loginUser, FindGroupUserWorkListRequest request){
        User targetUser = userJpaRepository.findById(request.getTargetUserId()).orElseThrow(() -> new UserException(UserErrorCode.NOT_EXISTS_USER));

        // 그룹 멤버 목록 조회
        List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberAllByGroupId(request.getUserGroupId());

        // 로그인한 사용자와 대상 사용자가 같은 그룹에 있는지 확인
        boolean isLoginUserInGroup = groupMembers.stream()
                .anyMatch(member -> member.getUser().getId().equals(loginUser.getId()));
        boolean isTargetUserInGroup = groupMembers.stream()
                .anyMatch(member -> member.getUser().getId().equals(targetUser.getId()));
        if (!isLoginUserInGroup || !isTargetUserInGroup) throw new WorkException(WorkErrorCode.ACCESS_DENIED);

        return workJpaRepository
                .findWorkListWithWorkTypeByTimeAndTargetUser(targetUser.getId(), request.getStartDate(), request.getEndDate())
                .stream()
                .map(Work::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UpComingCountWorkResponse> findUpComingWorkCount(User loginUser, LocalDate today, DateType dateType){
        if (dateType == DateType.WEEK) {
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            return workJpaRepository.findUpComingCountWork(loginUser.getId(), today, startOfWeek, endOfWeek);
        } else if (dateType == DateType.MONTH) {
            LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
            return workJpaRepository.findUpComingCountWork(loginUser.getId(), today, startOfMonth, endOfMonth);
        } else throw new WorkException(WorkErrorCode.FORMAT_NOT_SUPPORTED);
    }

    @Transactional(readOnly = true)
    public CompletedWorkCountResponse findCompletedWorkCount(User loginUser, LocalDate today, LocalDate targetMonth) {
        return CompletedWorkCountResponse.builder()
                .month(YearMonth.from(targetMonth).toString())
                .weeks(workJpaRepository.findWorksBeforeDate(loginUser.getId(), today, targetMonth)
                        .stream()
                        .collect(Collectors.groupingBy(
                                work -> {
                                    LocalDate firstDay = work.getWorkDate().with(TemporalAdjusters.firstDayOfMonth());
                                    LocalDate firstMonday = firstDay.getDayOfWeek() == DayOfWeek.MONDAY ? firstDay : firstDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                                    return (int) ChronoUnit.WEEKS.between(firstMonday, work.getWorkDate()) + 1;
                                },
                                Collectors.groupingBy(
                                        work -> work.getWorkType().getTitle(),
                                        Collectors.summingInt(work -> 1) // 근무 유형별 카운팅
                                )
                        )).entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(weekEntry -> new CompletedWorkCountWeekResponse(weekEntry.getKey(), weekEntry.getValue()))
                        .collect(Collectors.toList()))
                .build();
    }




}
