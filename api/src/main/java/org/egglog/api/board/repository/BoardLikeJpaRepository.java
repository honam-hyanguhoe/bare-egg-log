package org.egglog.api.board.repository;

import org.egglog.api.board.model.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeJpaRepository extends JpaRepository<BoardLike, Long> {
}
