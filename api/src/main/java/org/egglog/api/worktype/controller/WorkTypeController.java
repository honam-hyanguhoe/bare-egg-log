package org.egglog.api.worktype.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.model.dto.request.CreateWorkTypeRequest;
import org.egglog.api.worktype.model.dto.request.EditWorkTypeRequest;
import org.egglog.api.worktype.model.dto.response.WorkTypeResponse;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.egglog.api.worktype.model.service.WorkTypeService;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.workType.cotroller
 * fileName       : WorkTypeController
 * description    : 근무 타입 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-24|김도휘|최초 생성|
 * |2024-04-30|김형민|코드 리펙토링|
 * |2024-05-02|김형민|주석 추가|
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/worktypes")
public class WorkTypeController {

    private final WorkTypeService workTypeService;


    /**
     * 리스트 조회
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @return 리스트[근무타입ID, 이름, 색상, 근무 이미지, 태그 속성, 시작시각, 근무시간]
     * @author 김형민
     */
    @GetMapping("/list")
    public ResponseEntity<MessageUtils<List<WorkTypeResponse>>> getWorkTypeList(
            @AuthenticationPrincipal User loginUser
    ) {
        return ResponseEntity.ok()
                .body(MessageUtils.success(workTypeService.getWorkTypeList(loginUser)));
    }

    /**
     * 생성
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param request 이름, 색상, 근무 이미지, 시작시각, 근무시간
     * @return 근무타입ID, 이름, 색상, 근무 이미지, 태그 속성, 시작시각, 근무시간
     * @author 김형민
     */
    @PostMapping("/create")
    public ResponseEntity<MessageUtils<WorkTypeResponse>> registerWorkType(
            @AuthenticationPrincipal User loginUser,
            @RequestBody @Valid CreateWorkTypeRequest request) {
        return ResponseEntity.ok()
                .body(MessageUtils.success(workTypeService.createWorkType(loginUser, request)));
    }

    /**
     * 삭제
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @param workTypeId 삭제할 근무 유형 아이디
     * @return deleted massage
     * @author 김형민
     */
    @PatchMapping("/{workTypeId}")
    public ResponseEntity<MessageUtils> deleteWorkType(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Long workTypeId
    ) {
        workTypeService.deleteWorkType(loginUser, workTypeId);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    /**
     * 수정
     * @param loginUser 로그인 한 유저(jwt 토큰)
     * @param request 이름, 색상, 근무 이미지, 태그 속성, 시작시각, 근무시간
     * @param workTypeId 수정할 근무 유형 아이디
     * @return 수정된 근무타입ID, 이름, 색상, 근무 이미지, 태그 속성, 시작시각, 근무시간
     */
    @PatchMapping("/edit/{workTypeId}")
    public ResponseEntity<MessageUtils<WorkTypeResponse>> updateWorkType(
            @AuthenticationPrincipal User loginUser,
            @RequestBody @Valid EditWorkTypeRequest request,
            @PathVariable Long workTypeId
    ) {
        return ResponseEntity.ok().body(
                MessageUtils.success(workTypeService.editWorkType(loginUser, request, workTypeId)));
    }
}
