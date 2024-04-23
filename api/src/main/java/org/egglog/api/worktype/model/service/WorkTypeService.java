package org.egglog.api.worktype.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.WorkTypeJpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/worktypes")
public class WorkTypeService {

    private final WorkTypeJpaRepository workTypeJpaRepository;

    public List<WorkType> getWorkTypeList(Long userId) {
        List<WorkType> workTypesByUserId = workTypeJpaRepository.findWorkTypesByUserId(userId);

    }
}
