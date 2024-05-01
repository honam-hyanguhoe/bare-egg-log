package org.egglog.api.board.repository.jpa.comment;

import org.egglog.api.board.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomQuery {
}
