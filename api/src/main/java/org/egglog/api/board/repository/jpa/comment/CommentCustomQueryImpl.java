package org.egglog.api.board.repository.jpa.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.board.model.entity.QComment.comment;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.board.repository.jpa
 * fileName      : CommentCustomQueryImpl
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-30|김도휘|최초 생성|
 */

@Repository
@RequiredArgsConstructor
public class CommentCustomQueryImpl implements CommentCustomQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long getCommentCount(Long boardId) {
        Long count = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .fetchOne();

        return count;
    }

    @Override
    public Optional<List<Comment>> getCommentListByBoardId(Long boardId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(comment)
                .where(comment.board.id.eq(boardId))
                .fetch());
    }

    @Override
    public Optional<List<Comment>> getRecommentListByCommentId(Long commentId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(comment)
                .where(comment.parentId.eq(commentId))
                .fetch());

    }
}
