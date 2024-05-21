package org.egglog.api.board.repository.jpa.comment;

import org.egglog.api.board.model.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentCustomQuery {
    Long getCommentCount(Long boardId);

    Optional<List<Comment>> getCommentListByBoardId(Long boardId);

    Optional<List<Comment>> getRecommentListByCommentId(Long commentId);

    Optional<Comment> findWithUserById(Long commentId);
}
