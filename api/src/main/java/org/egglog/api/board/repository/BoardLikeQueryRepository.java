package org.egglog.api.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.entity.BoardLike;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.egglog.api.board.model.entity.QBoardLike.boardLike;

@Repository
@RequiredArgsConstructor
public class BoardLikeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<BoardLike> getBoardLikeByBoardId(Long boardId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(boardLike)
                .where(boardLike.board.id.eq(boardId))
                .fetchOne());
    }

}
