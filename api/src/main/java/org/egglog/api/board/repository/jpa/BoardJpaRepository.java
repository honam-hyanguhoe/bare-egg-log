package org.egglog.api.board.repository.jpa;

import org.egglog.api.board.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b.id FROM Board b ORDER BY b.createdAt DESC")
    List<Long> findTop2ByOrderByCreatedAtDesc();
}
