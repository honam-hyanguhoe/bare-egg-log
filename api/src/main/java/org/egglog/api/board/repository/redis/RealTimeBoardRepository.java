package org.egglog.api.board.repository.redis;

import org.egglog.api.board.model.entity.RealTimeBoard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RealTimeBoardRepository extends CrudRepository<RealTimeBoard, Long> {
    List<RealTimeBoard> findAll();
    Optional<RealTimeBoard> findByBoardId(Long boardId);
}
