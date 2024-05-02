package org.egglog.api.work.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.work.model.dto.request.*;
import org.egglog.api.work.model.dto.response.upcoming.enums.DateType;
import org.egglog.api.work.model.service.WorkService;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


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
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/work")
public class WorkController {
    private final WorkService workService;

    @PostMapping("/create")
    public ResponseEntity<MessageUtils> createWork(
            @AuthenticationPrincipal User loginUser,
            @RequestBody CreateWorkListRequest request
    ){
        workService.createWork(loginUser, request);
        return ResponseEntity.ok().body(
                MessageUtils.success());
    }

    @PatchMapping("/update")
    public ResponseEntity<MessageUtils> updateWork(
            @AuthenticationPrincipal User loginUser,
            @RequestBody EditAndDeleteWorkListRequest request
    ){
        workService.updateWork(loginUser, request);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @GetMapping("/find")
    public ResponseEntity<MessageUtils> findWorkList(
            @AuthenticationPrincipal User loginUser,
            @ModelAttribute FindWorkListRequest request
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.findWorkList(loginUser, request)));
    }

    @GetMapping("/find/user")
    public ResponseEntity<MessageUtils> findGroupUserWorkList(
            @AuthenticationPrincipal User loginUser,
            @ModelAttribute FindGroupUserWorkListRequest request
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.findGroupUserWorkList(loginUser, request)));
    }

    @GetMapping("/find/upcoming/{dateType}")
    public ResponseEntity<MessageUtils> findUpcomingCountWorkList(
            @AuthenticationPrincipal User loginUser,
            @RequestParam("today")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today,
            @PathVariable DateType dateType
            ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.findUpComingWorkCount(loginUser, today, dateType)));
    }

    @GetMapping("/find/completed")
    public ResponseEntity<MessageUtils> findCompletedCountWorkList(
            @AuthenticationPrincipal User loginUser,
            @RequestParam("today")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today,
            @RequestParam("month")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(workService.findCompletedWorkCount(loginUser, today, month)));
    }
}
