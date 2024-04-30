package org.egglog.api.worktype.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.exception.WorkTypeException;
import org.egglog.api.worktype.model.dto.request.CreateWorkTypeRequest;
import org.egglog.api.worktype.model.dto.request.EditWorkTypeRequest;
import org.egglog.api.worktype.model.dto.response.WorkTypeResponse;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.jpa.WorkTypeJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.egglog.api.worktype.exception.WorkTypeErrorCode.*;
import static org.egglog.api.worktype.exception.WorkTypeErrorCode.ACCESS_DENIED;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTypeService {

    private final WorkTypeJpaRepository workTypeJpaRepository;


    @Transactional
    public WorkTypeResponse createWorkType(User loginUser, CreateWorkTypeRequest request){
        return workTypeJpaRepository.save(request.toEntity(loginUser)).toResponse();
    }

    @Transactional
    public WorkTypeResponse editWorkType(User loginUser, EditWorkTypeRequest request){
        WorkType workType = workTypeJpaRepository.findWithUserById(request.getWorkTypeId())
                .orElseThrow(() -> new WorkTypeException(NO_EXIST_WORKTYPE));
        if (workType.getUser().getId()!=loginUser.getId()) throw new WorkTypeException(ACCESS_DENIED);
        return workTypeJpaRepository.save(workType
                    .edit(request.getTitle(), request.getWorkTag(), request.getColor(), request.getWorkTypeImgUrl(), request.getStartTime(), request.getEndTime()))
                .toResponse();
    }

    @Transactional
    public void deleteWorkType(User loginUser, Long workTypeId){
        WorkType workType = workTypeJpaRepository.findWithUserById(workTypeId)
                .orElseThrow(() -> new WorkTypeException(NO_EXIST_WORKTYPE));
        if (workType.getUser().getId()!=loginUser.getId()) throw new WorkTypeException(ACCESS_DENIED);
        workTypeJpaRepository.delete(workType);
    }

    @Transactional
    public List<WorkTypeResponse> getWorkTypeList(User loginUser){
        return workTypeJpaRepository.findWorkTypesByUserId(loginUser.getId())
                .stream()
                .map(WorkType::toResponse)
                .collect(Collectors.toList());
    }
}
