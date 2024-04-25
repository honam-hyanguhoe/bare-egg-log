package org.egglog.api.board.repository.jpa;

import org.egglog.api.board.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}