package org.egglog.api.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.user.model.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.board.model.entity.QBoard.board;
import static org.egglog.api.board.model.entity.QBoardLike.boardLike;
import static org.egglog.api.user.model.entity.QUsers.users;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(board)
                .where(board.id.eq(boardId))
                .fetchOne());
    }

    public List<Board> findBoardList(Long lastBoardId) {
        return jpaQueryFactory
                .selectFrom(board)
                .leftJoin(board, boardLike.board)
                .where(ltBoardId(lastBoardId))
                .orderBy(board.id.desc())
                .fetch();
    }

    // id < 첫 번째 조회에서는 파라미터를 사용하지 않기 위한 동적 쿼리
    private BooleanExpression ltBoardId(Long lastBoardId) {
        if (lastBoardId == null) {
            return null;    // BooleanExpression 자리에 null이 반환되면 조건문에서 자동을 제외된다.
        }
        return board.id.lt(lastBoardId);
    }

}
