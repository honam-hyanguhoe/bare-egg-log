package org.egglog.api.board.repository.jpa.boardLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.entity.BoardLike;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.board.model.entity.QBoardLike.boardLike;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.board.repository.jpa
 * fileName      : BoardLikeCustomQueryImpl
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-30|김도휘|최초 생성|
 */
@Repository
@RequiredArgsConstructor
public class BoardLikeCustomQueryImpl implements BoardLikeCustomQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<BoardLike> getBoardLikeByBoardId(Long boardId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(boardLike)
                .where(boardLike.board.id.eq(boardId))
                .fetchOne());
    }

    public Optional<List<BoardLike>> getBoardLikeListByBoardId(Long boardId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(boardLike)
                .where(boardLike.board.id.eq(boardId))
                .fetch()
        );
    }

}
