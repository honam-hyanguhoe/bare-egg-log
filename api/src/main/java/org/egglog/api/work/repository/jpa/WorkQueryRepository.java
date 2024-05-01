package org.egglog.api.work.repository.jpa;

import org.egglog.api.work.model.dto.response.upcoming.UpComingCountWorkResponse;
import org.egglog.api.work.model.entity.Work;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkQueryRepository {
    Optional<Work> findWorkWithUserByWorkId(Long workId);
    Optional<Work> findWorkWithAllByWorkId(Long workId);
    Optional<Work> findWorkWithCalendarGroupByWorkId(Long workId);
    Optional<Work> findWorkWithWorkTypeByWorkId(Long workId);
    List<Work> findWorkListWithWorkTypeByTime(Long calendarGroupId, LocalDate startDate, LocalDate endDate);
    List<Work> findWorkListWithWorkTypeByTimeAndTargetUser(Long targetUserId, LocalDate startDate, LocalDate endDate);
    List<UpComingCountWorkResponse> findUpComingCountWork(Long userId, LocalDate today, LocalDate startDate, LocalDate endDate);
    List<Work> findWorksBeforeDate(Long userId, LocalDate date);
}
