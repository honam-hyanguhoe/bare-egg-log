package org.egglog.api.worktype.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.alarm.model.entity.Alarm;
import org.egglog.api.alarm.model.service.AlarmService;
import org.egglog.api.alarm.repository.jpa.AlarmRepository;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.exception.WorkTypeException;
import org.egglog.api.worktype.model.dto.request.CreateWorkTypeRequest;
import org.egglog.api.worktype.model.dto.request.EditWorkTypeRequest;
import org.egglog.api.worktype.model.dto.response.WorkTypeResponse;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.jpa.WorkTypeJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.egglog.api.worktype.exception.WorkTypeErrorCode.*;
import static org.egglog.api.worktype.exception.WorkTypeErrorCode.ACCESS_DENIED;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTypeService {

    private final WorkTypeJpaRepository workTypeJpaRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public WorkTypeResponse createWorkType(User loginUser, CreateWorkTypeRequest request){
        log.debug(" ==== ==== ==== [ 근무 타입 생성 서비스 실행 ] ==== ==== ====");
        WorkType workType = workTypeJpaRepository.save(request.toEntity(loginUser));
        alarmRepository.save(workType.makeDefaultAlarm());
        return workType.toResponse();
    }

    @Transactional
    public WorkTypeResponse editWorkType(User loginUser, EditWorkTypeRequest request, Long workTypeId){
        log.debug(" ==== ==== ==== [ 근무 타입 수정 서비스 실행 ] ==== ==== ====");
        WorkType workType = workTypeJpaRepository.findWithUserById(workTypeId)
                .orElseThrow(() -> new WorkTypeException(NO_EXIST_WORKTYPE));
        if (workType.getUser().getId()!=loginUser.getId()) throw new WorkTypeException(ACCESS_DENIED);

        return workTypeJpaRepository.save(workType
                    .edit(request.getTitle(), request.getColor(), request.getWorkTypeImgUrl(), request.getStartTime(), request.getWorkTime()))
                .toResponse();
    }

    @Transactional
    public void deleteWorkType(User loginUser, Long workTypeId){
        log.debug(" ==== ==== ==== [ 근무 타입 삭제 서비스 실행 ] ==== ==== ==== ");
        WorkType workType = workTypeJpaRepository.findWithUserById(workTypeId)
                .orElseThrow(() -> new WorkTypeException(NO_EXIST_WORKTYPE));

        if ( workType.getWorkTag().equals(WorkTag.DELETE) || !(workType.getWorkTag().equals(WorkTag.ETC)) || (workType.getUser().getId()!=loginUser.getId())) {
            throw new WorkTypeException(ACCESS_DENIED);
        }
        workTypeJpaRepository.save(workType.delete());
        Optional<Alarm> byWorkTypeId = alarmRepository.findByWorkTypeId(workTypeId);
        if (byWorkTypeId.isPresent()) {
            alarmRepository.delete(byWorkTypeId.get());
        }
    }

    @Transactional
    public List<WorkTypeResponse> getWorkTypeList(User loginUser){
        log.debug(" ==== ==== ==== [ 근무 타입 리스트 조회 서비스 실행 ] ==== ==== ====");
        return workTypeJpaRepository.findListByUserId(loginUser.getId())
                .stream()
                .map(WorkType::toResponse)
                .collect(Collectors.toList());
    }
}
