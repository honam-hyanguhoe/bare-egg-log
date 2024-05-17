package org.egglog.api.work.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.util.RandomUidGenerator;
import org.egglog.api.alarm.model.entity.Alarm;
import org.egglog.api.alarm.repository.jpa.AlarmRepository;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.model.service.GroupService;
import org.egglog.api.group.repository.jpa.GroupMemberRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.egglog.api.work.exception.WorkErrorCode;
import org.egglog.api.work.exception.WorkException;
import org.egglog.api.work.model.dto.request.*;
import org.egglog.api.work.model.dto.response.WorkWithAlarmListResponse;
import org.egglog.api.work.model.dto.response.WorkWithAlarmResponse;
import org.egglog.api.work.model.dto.response.completed.CompletedWorkCountWeekResponse;
import org.egglog.api.work.model.dto.response.upcoming.UpComingCountWorkResponse;
import org.egglog.api.work.model.dto.response.WorkListResponse;
import org.egglog.api.work.model.dto.response.WorkResponse;
import org.egglog.api.work.model.dto.response.completed.CompletedWorkCountResponse;
import org.egglog.api.work.model.dto.response.upcoming.enums.DateType;
import org.egglog.api.work.model.entity.Work;
import org.egglog.api.work.repository.jpa.WorkJpaRepository;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.jpa.WorkTypeJpaRepository;
import org.egglog.utility.utils.MonthUtils;
import org.egglog.utility.utils.MonthUtils.Month;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collector;
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
    private final AlarmRepository alarmRepository;
    private final GroupService groupService;

    @Transactional
    public List<WorkResponse> createWork(User loginUser, CreateWorkListRequest request){
        log.debug(" ==== ==== ==== [ 근무 일정 생성 서비스 실행 ] ==== ==== ====");
        if (loginUser.getWorkGroupId()!= request.getCalendarGroupId()) throw new CalendarGroupException(CalendarGroupErrorCode.WORK_GROUP_ACCESS_DENY);
        CalendarGroup calendarGroup = calendarGroupRepository.findById(request.getCalendarGroupId())
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));

        if (calendarGroup.getUser().getId()!=loginUser.getId()) throw new WorkException(WorkErrorCode.ACCESS_DENIED);

        Map<Long, WorkType> userWorkTypeMap = workTypeJpaRepository
                .findByUser(loginUser).stream().collect(Collectors.toMap(WorkType::getId, wt -> wt));

        return workJpaRepository.saveAll(
                        request.getWorkTypes().stream()
                                .map(value-> value.toEntity(loginUser, userWorkTypeMap, calendarGroup))
                                .collect(Collectors.toList()))
                .stream()
                .map(Work::toResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public List<WorkResponse> syncWork(User loginUser, SyncExcelWorkRequest request){
        log.debug(" ==== ==== ==== [ 근무 일정 동기화 서비스 실행 ] ==== ==== ====");

        CalendarGroup calendarGroup = calendarGroupRepository.findById(loginUser.getWorkGroupId())
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));

        if (calendarGroup.getUser().getId()!=loginUser.getId()) throw new WorkException(WorkErrorCode.ACCESS_DENIED);


        Map<LocalDate, WorkType> excelDataMap = groupService.getUserExcelData(request.getGroupId(), request.getTargetMonth(), request.getIndex(), loginUser);

        LocalDate startOfMonth = request.getTargetMonth().withDayOfMonth(1);
        LocalDate endOfMonth = request.getTargetMonth().withDayOfMonth(request.getTargetMonth().lengthOfMonth());

        List<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDay = startOfMonth;
        while (!currentDay.isAfter(endOfMonth)) {
            dateList.add(currentDay);
            currentDay = currentDay.plusDays(1);
        }

        Map<LocalDate, Work> userDateMap = workJpaRepository.findWorkListWithAllByTime(loginUser.getWorkGroupId(), startOfMonth, endOfMonth)
                .stream()
                .collect(Collectors.toMap(Work::getWorkDate, work -> work));


        List<Work> updateWorkList = new ArrayList<>();
        List<Work> deletedWorkList = new ArrayList<>();
        for (LocalDate currentLocalDate : dateList) {
            if (userDateMap.containsKey(currentLocalDate) && excelDataMap.containsKey(currentLocalDate)) {
                // 엑셀 데이터 존재, 기존 사용자 근무 존재 -> 해당 값 업데이트
                updateWorkList.add(userDateMap.get(currentLocalDate).updateWorkType(excelDataMap.get(currentLocalDate)));
            } else if (!userDateMap.containsKey(currentLocalDate) && excelDataMap.containsKey(currentLocalDate)) {
                // 엑셀 데이터 존재, 기존 사용자 근무 존재 x -> 새 엔티티 추가
                updateWorkList.add(Work.builder()
                        .workDate(currentLocalDate)
                        .workType(excelDataMap.get(currentLocalDate))
                        .calendarGroup(calendarGroup)
                        .user(loginUser)
                        .uuid(new RandomUidGenerator().generateUid().getValue())
                        .build());
            } else if (userDateMap.containsKey(currentLocalDate) && !excelDataMap.containsKey(currentLocalDate)) {
                // 엑셀 데이터 존재x, 기존 사용자 근무 존재 -> 해당 값 삭제
                deletedWorkList.add(userDateMap.get(currentLocalDate));
            }
            // 엑셀 데이터 존재 x, 기존 사용자 근무 존재 x -> 아무것도 안한다.
        }
        workJpaRepository.deleteAll(deletedWorkList);
        return workJpaRepository.saveAll(updateWorkList).stream().map(Work::toResponse).collect(Collectors.toList());
    }


    @Transactional
    public List<WorkResponse> updateWork(User loginUser, EditAndDeleteWorkListRequest request){
        log.debug(" ==== ==== ==== [ 유저의 근무 일정 수정 및 삭제 서비스 실행 ] ==== ==== ====");
        if (loginUser.getWorkGroupId()!= request.getCalendarGroupId()) throw new CalendarGroupException(CalendarGroupErrorCode.WORK_GROUP_ACCESS_DENY);
        CalendarGroup calendarGroup = calendarGroupRepository.findCalendarGroupWithUserById(request.getCalendarGroupId())
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));
        if (calendarGroup.getUser().getId()!=loginUser.getId()) throw new WorkException(WorkErrorCode.ACCESS_DENIED);
        Map<Long, WorkType> userWorkTypeMap = workTypeJpaRepository
                .findByUser(loginUser).stream().collect(Collectors.toMap(WorkType::getId, wt -> wt));
        //EditAndDeleteWorkListRequest 내에 있는 EditAndDeleteWorkRequest의 isDeleted이 true 이면 삭제, 그게 아니라면 새 데이터 저장
        return workJpaRepository.saveAll(request.getEditWorkList().stream().flatMap(editRequest -> {
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
        })
        .collect(Collectors.toList()))
                .stream()
                .map(Work::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkListResponse findWorkList(User loginUser, FindWorkListRequest request){
        log.debug(" ==== ==== ==== [ 로그인 유저의 근무 일정 정보 조회 서비스 실행 ] ==== ==== ====");
        CalendarGroup calendarGroup = calendarGroupRepository.findCalendarGroupWithUserById(loginUser.getWorkGroupId())
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));
        if (calendarGroup.getUser().getId()!=loginUser.getId()) throw new WorkException(WorkErrorCode.ACCESS_DENIED);
        List<WorkResponse> workResponses = workJpaRepository
                .findWorkListWithAllByTime(calendarGroup.getId(), request.getStartDate(), request.getEndDate())
                .stream()
                .map(Work::toResponse)
                .collect(Collectors.toList());
        return WorkListResponse.builder()
                .calendarGroup(calendarGroup.toResponse())
                .workList(workResponses)
                .build();
    }


    @Transactional(readOnly = true)
    public WorkWithAlarmListResponse findWorkWithAlarmList(User loginUser, FindWorkListRequest request){
        log.debug(" ==== ==== ==== [ 로그인 유저의 근무 일정 정보 및 알람 조회 서비스 실행 ] ==== ==== ====");
        CalendarGroup calendarGroup = calendarGroupRepository.findCalendarGroupWithUserById(loginUser.getWorkGroupId())
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));
        if (calendarGroup.getUser().getId()!=loginUser.getId()) throw new WorkException(WorkErrorCode.ACCESS_DENIED);

        Map<WorkType, Alarm> alarmMap = alarmRepository.findAlarmListByUserId(loginUser.getId()).get()
                .stream().collect(Collectors.toMap(Alarm::getWorkType, alarm -> alarm));

        List<WorkWithAlarmResponse> res = workJpaRepository
                .findWorkListWithAllByTime(calendarGroup.getId(), request.getStartDate(), request.getEndDate())
                .stream()
                .map(value -> value.toResponse(alarmMap))
                .collect(Collectors.toList());

        return WorkWithAlarmListResponse.builder()
                .calendarGroup(calendarGroup.toResponse())
                .workList(res)
                .build();
    }

    @Transactional(readOnly = true)
    public List<WorkResponse> findGroupUserWorkList(User loginUser, FindGroupUserWorkListRequest request){
        log.debug(" ==== ==== ==== [ 그룹내 유저의 근무 일정 정보 조회 서비스 실행 ] ==== ==== ====");
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
        log.debug(" ==== ==== ==== [ 주, 월 남은 근무 일정 조회 서비스 실행 ] ==== ==== ====");
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
        log.debug(" ==== ==== ==== [ 완료한 근무 일정 통계 조회 서비스 실행 ] ==== ==== ====");
        Map<String, Integer> initialWorkCounts = Map.of(
                WorkTag.DAY.name(), 0,
                WorkTag.EVE.name(), 0,
                WorkTag.NIGHT.name(), 0,
                WorkTag.OFF.name(), 0
        );
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
                                Collector.of(
                                        () -> new HashMap<>(initialWorkCounts),
                                        (acc, work) -> acc.merge(work.getWorkType().getTitle(), 1, Integer::sum),
                                        (acc1, acc2) -> {
                                            acc2.forEach((key, value) -> acc1.merge(key, value, Integer::sum));
                                            return acc1;
                                        }
                                )
                        ))
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(weekEntry -> new CompletedWorkCountWeekResponse(weekEntry.getKey(), weekEntry.getValue()))
                        .collect(Collectors.toList()))
                .build();
    }




}
