package org.egglog.api.work.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.work.model.dto.request.*;
import org.egglog.api.work.model.dto.response.WorkListResponse;
import org.egglog.api.work.model.dto.response.WorkResponse;
import org.egglog.api.work.model.dto.response.WorkWithAlarmListResponse;
import org.egglog.api.work.model.dto.response.completed.CompletedWorkCountResponse;
import org.egglog.api.work.model.dto.response.upcoming.UpComingCountWorkResponse;
import org.egglog.api.work.model.dto.response.upcoming.enums.DateType;
import org.egglog.api.work.model.service.WorkService;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.controller
 * fileName      : WorkController
 * description    : 근무 일정을 추가, 수정, 삭제, 조회 하는 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-29|김형민|최초 생성|
 * |2024-05-01|김형민|통계 조회 추가|
 * |2024-05-02|김형민|주석 추가|
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/work")
public class WorkController {
    private final WorkService workService;

    /**
     * 근무 일정 생성
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param request 캘린더 그룹 ID, List[{근무 타입 ID, 근무 일}]
     * @return 생성된 근무 일정 객체 응답 리스트
     * @author 김형민
     */
    @PostMapping("/create")
    public ResponseEntity<MessageUtils<List<WorkResponse>>> createWork(
            @AuthenticationPrincipal User loginUser,
            @RequestBody @Valid CreateWorkListRequest request
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.createWork(loginUser, request)));
    }

    /**
     * 근무 일정 수정 및 삭제
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param request 캘린더 그룹 ID, List[{변경할 근무 ID, 근무 일,  근무 타입 ID, 삭제 여부}]
     * @return 수정된 근무 일정 객체 응답 리스트
     * @author 김형민
     */
    @PatchMapping("/update")
    public ResponseEntity<MessageUtils<List<WorkResponse>>> updateWork(
            @AuthenticationPrincipal User loginUser,
            @RequestBody @Valid EditAndDeleteWorkListRequest request
    ){
        return ResponseEntity.ok().body(MessageUtils.success(workService.updateWork(loginUser, request)));
    }

    /**
     * 근무 일정 조회
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param request  캘린더 그룹 ID, 조회할 시작 일, 조회할 마지막 일
     * @return 조회 된 근무 일정 객체 응답 리스트
     * @author 김형민
     */
    @GetMapping("/find")
    public ResponseEntity<MessageUtils<WorkListResponse>> findWorkList(
            @AuthenticationPrincipal User loginUser,
            @ModelAttribute @Valid FindWorkListRequest request
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.findWorkList(loginUser, request)));
    }

    /**
     * 같은 그룹 유저의 근무 일정 조회
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param request userGroupId(현재 유저 그룹 ID), targetUserId(조회할 유저의 ID), startDate(조회할 시작 일), endDate(조회할 마지막 일)
     * @return 조회 된 근무 일정 객체 응답 리스트
     * @author 김형민
     */
    @GetMapping("/find/user")
    public ResponseEntity<MessageUtils<List<WorkResponse>>> findGroupUserWorkList(
            @AuthenticationPrincipal User loginUser,
            @ModelAttribute @Valid FindGroupUserWorkListRequest request
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.findGroupUserWorkList(loginUser, request)));
    }


    /**
     * 근무 일정 조회
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param request  캘린더 그룹 ID, 조회할 시작 일, 조회할 마지막 일
     * @return 조회 된 근무 일정 객체 응답 리스트
     * @author 김형민
     */
    @GetMapping("/alarm-find")
    public ResponseEntity<MessageUtils<WorkWithAlarmListResponse>> findWorkWithAlarmList(
            @AuthenticationPrincipal User loginUser,
            @ModelAttribute @Valid FindWorkListRequest request
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.findWorkWithAlarmList(loginUser, request)));
    }






    /**
     * 주, 월의 이번주 남은 근무 당 개수
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param today 오늘(YYYY-MM-DD)
     * @param dateType WEEK, MONTH
     * @return 리스트 [ 근무이름, 해당 남은 근무의 개수, 해당 근무의 색 ]
     * @author 김형민
     */
    @GetMapping("/find/upcoming/{dateType}")
    public ResponseEntity<MessageUtils<List<UpComingCountWorkResponse>>> findUpcomingCountWorkList(
            @AuthenticationPrincipal User loginUser,
            @RequestParam(value = "today", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today,
            @PathVariable DateType dateType
            ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.findUpComingWorkCount(loginUser, today, dateType)));
    }


    /**
     * 이제까지 수행한 근무의 통계
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param today 오늘(YYYY-MM-DD)
     * @param month 조회할 타겟 월(YYYY-MM-DD)
     * @return CompletedWorkCountResponse
     * @author 김형민
     */
    @GetMapping("/find/completed")
    public ResponseEntity<MessageUtils<CompletedWorkCountResponse>> findCompletedCountWorkList(
            @AuthenticationPrincipal User loginUser,
            @RequestParam(value = "today", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today,
            @RequestParam(value = "month", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.findCompletedWorkCount(loginUser, today, month)));
    }
}
