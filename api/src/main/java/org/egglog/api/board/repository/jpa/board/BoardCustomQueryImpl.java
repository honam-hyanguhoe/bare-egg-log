package org.egglog.api.board.repository.jpa.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.model.entity.BoardLike;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.board.model.entity.QBoard.board;
import static org.egglog.api.board.model.entity.QBoardLike.boardLike;
import static org.egglog.api.board.model.entity.QComment.comment;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.board.repository.jpa
 * fileName      : BoardCustomQueryImpl
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-30|김도휘|최초 생성|
 */
@Repository
@RequiredArgsConstructor
public class BoardCustomQueryImpl implements BoardCustomQuery {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 급상승 게시판<br>
     * [로직] 좋아요 + 조회수 상위 2개 게시물 (하루 범위 )
     *
     * @param groupId
     * @param hospitalId
     * @return
     */
    @Override
    public List<Board> findBoardHotList(Long groupId, Long hospitalId) {
        BooleanBuilder builder = new BooleanBuilder();

        if (groupId == null && hospitalId == null) {
            // 둘 다 null인 경우 특정 로직 처리
            builder.and(board.group.id.isNull())
                    .and(board.hospital.id.isNull());
        } else {
            if (groupId != null) {
                builder.and(board.group.id.eq(groupId));
            }
            if (hospitalId != null) {
                builder.and(board.hospital.id.eq(hospitalId));
            }
        }

        // 댓글 수와 좋아요 수를 합산하는 표현식
        NumberTemplate<Long> sumOfCommentsAndLikes = Expressions.numberTemplate(Long.class,
                "({0} + {1})",
                JPAExpressions.select(comment.count())
                        .from(comment)
                        .where(comment.board.id.eq(board.id)),
                JPAExpressions.select(boardLike.count())
                        .from(boardLike)
                        .where(boardLike.board.id.eq(board.id)));

        return jpaQueryFactory
                .selectFrom(board)
                .where(builder) // 기존 조건을 유지
                .orderBy(sumOfCommentsAndLikes.desc()) // 댓글 수와 좋아요 수 합산을 기준으로 내림차순 정렬
                .limit(2) // 상위 2개 게시물 선택
                .fetch();

    }


    /**
     * 좋아요 개수
     *
     * @param boardId
     * @return
     */
    @Override
    public Long getLikeCount(Long boardId) {
        Long count = jpaQueryFactory
                .select(boardLike.count())
                .from(boardLike)
                .where(boardLike.board.id.eq(boardId))
                .fetchOne();

        return count;
    }

    @Override
    public Optional<BoardLike> getUserBoardLike(Long boardId, Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(boardLike)
                .where(
                        boardLike.user.id.eq(userId),
                        boardLike.board.id.eq(boardId)
                )
                .fetchOne());

    }

}
