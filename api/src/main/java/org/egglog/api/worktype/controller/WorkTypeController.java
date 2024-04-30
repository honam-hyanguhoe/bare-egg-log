package org.egglog.api.worktype.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.model.dto.request.CreateWorkTypeRequest;
import org.egglog.api.worktype.model.dto.request.EditWorkTypeRequest;
import org.egglog.api.worktype.model.dto.request.WorkTypeForm;
import org.egglog.api.worktype.model.dto.request.WorkTypeModifyForm;
import org.egglog.api.worktype.model.service.WorkTypeService;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/worktypes")
public class WorkTypeController {

    private final WorkTypeService workTypeService;

    /**
     * 리스트 조회
     */
    @GetMapping("/list")
    public ResponseEntity<?> getWorkTypeList(
            @AuthenticationPrincipal User loginUser
    ) {
        return ResponseEntity.ok()
                .body(MessageUtils.success(workTypeService.getWorkTypeList(loginUser)));
    }

    /**
     * 삭제
     */
    @PostMapping("/create")
    public ResponseEntity<MessageUtils> registerWorkType(
            @AuthenticationPrincipal User loginUser,
            @RequestBody CreateWorkTypeRequest request) {
        return ResponseEntity.ok()
                .body(MessageUtils.success(workTypeService.createWorkType(loginUser, request)));
    }

    /**
     * 삭제
     */
    @DeleteMapping("/{workTypeId}")
    public ResponseEntity<MessageUtils> deleteWorkType(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Long workTypeId
    ) {
        workTypeService.deleteWorkType(loginUser, workTypeId);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    /**
     * 수정 (덮어쓰기)
     */
    @PutMapping("/edit")
    public ResponseEntity<MessageUtils> updateWorkType(
            @AuthenticationPrincipal User loginUser,
            @RequestBody EditWorkTypeRequest request
    ) {
        return ResponseEntity.ok().body(
                MessageUtils.success(workTypeService.editWorkType(loginUser, request)));
    }
}
