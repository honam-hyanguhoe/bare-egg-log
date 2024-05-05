package org.egglog.api.board.repository.jpa.board;

import org.egglog.api.board.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomQuery {
    @Query("SELECT b.id FROM Board b ORDER BY b.createdAt DESC")
    List<Long> findTop2ByOrderByCreatedAtDesc();

}
