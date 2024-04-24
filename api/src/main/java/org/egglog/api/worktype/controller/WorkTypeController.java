package org.egglog.api.worktype.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.model.dto.params.WorkTypeForm;
import org.egglog.api.worktype.model.dto.params.WorkTypeModifyForm;
import org.egglog.api.worktype.model.service.WorkTypeService;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/worktypes")
public class WorkTypeController {

    private final WorkTypeService workTypeService;

    @GetMapping("")
    public ResponseEntity<?> getWorkTypeList() {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(workTypeService.getWorkTypeList(userId)));
    }

    /**
     * 등록
     *
     * @param workTypeForm
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> registerWorkType(@RequestBody WorkTypeForm workTypeForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        workTypeService.registerBoard(workTypeForm);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }


    @DeleteMapping("/{work_type_id}")
    public ResponseEntity deleteWorkType(@PathVariable("work_type_id") Long workTypeId
//            TODO @AuthenticationPrincipal User user
    ) {
        User user = null;
        workTypeService.deleteWorkType(workTypeId);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }


    @PatchMapping("/{work_type_id}")
    public ResponseEntity updateWorkType(@PathVariable("work_type_id") Long workTypeId,
            @RequestBody WorkTypeModifyForm workTypeModifyForm
//            TODO @AuthenticationPrincipal User user
    ) {
        Long userId = 1L;
        return ResponseEntity.ok().body(
                MessageUtils.success(workTypeService.modifyBoard(workTypeId, workTypeModifyForm)));
    }
}
