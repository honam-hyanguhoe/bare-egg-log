package org.egglog.api.board.repository.jpa.boardLike;

import org.egglog.api.board.model.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long>, BoardLikeCustomQuery {
}
