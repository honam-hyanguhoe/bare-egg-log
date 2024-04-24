package org.egglog.api.worktype.model.service;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.board.exception.BoardErrorCode;
import org.egglog.api.board.exception.BoardException;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.UserQueryRepository;
import org.egglog.api.worktype.exception.WorkTypeErrorCode;
import org.egglog.api.worktype.exception.WorkTypeException;
import org.egglog.api.worktype.model.dto.params.WorkTypeForm;
import org.egglog.api.worktype.model.dto.params.WorkTypeModifyForm;
import org.egglog.api.worktype.model.dto.response.WorkTypeListOutputSpec;
import org.egglog.api.worktype.model.dto.response.WorkTypeOutputSpec;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.WorkTypeJpaRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTypeService {

    private final WorkTypeJpaRepository workTypeJpaRepository;

    private final UserQueryRepository userQueryRepository;

    public List<WorkTypeListOutputSpec> getWorkTypeList(Long userId) {
        List<WorkTypeListOutputSpec> workTypeListOutputSpecList = new ArrayList<>();
        List<WorkType> workTypesByUserId = workTypeJpaRepository.findWorkTypesByUserId(userId);

        for (WorkType workType : workTypesByUserId) {
            WorkTypeListOutputSpec workTypeListOutputSpec = WorkTypeListOutputSpec.builder()
                    .id(workType.getId())
                    .name(workType.getTitle())
                    .imgUrl(workType.getWorkTypeImgUrl())
                    .color(workType.getColor())
                    .startTime(workType.getStartTime())
                    .endTime(workType.getEndTime())
                    .build();

            workTypeListOutputSpecList.add(workTypeListOutputSpec);
        }
        return workTypeListOutputSpecList;
    }


    public void registerBoard(WorkTypeForm workTypeForm) {
        User user = userQueryRepository.findById(workTypeForm.getUserId()).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        WorkType workType = WorkType.builder()
                .title(workTypeForm.getTitle())
                .color(workTypeForm.getColor())
                .workTypeImgUrl(workTypeForm.getImgUrl())
                .startTime(workTypeForm.getStartTime())
                .endTime(workTypeForm.getEndTime())
                .user(user)
                .build();

        try {
            workTypeJpaRepository.save(workType);    //저장

        } catch (PersistenceException e) {
            throw new BoardException(BoardErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }

    @Transactional
    public WorkTypeOutputSpec modifyBoard(Long workTypeId, WorkTypeModifyForm workTypeModifyForm) {
        User user = userQueryRepository.findById(workTypeModifyForm.getUserId()).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        WorkType workType = workTypeJpaRepository.findById(workTypeModifyForm.getId()).orElseThrow(
                () -> new WorkTypeException(WorkTypeErrorCode.NO_EXIST_WORKTYPE)
        );

        WorkTypeOutputSpec workTypeOutputSpec = WorkTypeOutputSpec.builder()
                .id(workTypeModifyForm.getId())
                .name(workTypeModifyForm.getTitle())
                .imgUrl(workType.getWorkTypeImgUrl())
                .color(workTypeModifyForm.getColor())
                .startTime(workTypeModifyForm.getStartTime())
                .endTime(workTypeModifyForm.getEndTime())
                .build();

        workType.setTitle(workTypeModifyForm.getTitle());
        workType.setColor(workTypeModifyForm.getColor());

        return workTypeOutputSpec;
    }

    @Transactional
    public void deleteWorkType(Long workTypeId) {
        WorkType workType = workTypeJpaRepository.findById(workTypeId).orElseThrow(
                () -> new WorkTypeException(WorkTypeErrorCode.NO_EXIST_WORKTYPE)
        );

        try {
            workTypeJpaRepository.delete(workType);

        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }
}
