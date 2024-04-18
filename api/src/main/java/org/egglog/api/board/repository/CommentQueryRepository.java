package org.egglog.api.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static org.egglog.api.board.model.entity.QBoardHit.boardHit;
import static org.egglog.api.board.model.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Long getCommentCount(Long boardId) {
        Long count = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .fetchOne();

        return count;
    }
}
