package org.egglog.api.work.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.work.model.dto.request.CreateAndEditWorkListRequest;
import org.egglog.api.work.model.service.WorkService;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
            @AuthenticationPrincipal User user,
            @RequestBody CreateAndEditWorkListRequest request
            ){
        workService.createWork(user, request);
        return ResponseEntity.ok().body(
                MessageUtils.success());
    }
}
