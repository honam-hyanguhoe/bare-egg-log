package org.egglog.api.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.user.model.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.egglog.api.board.model.entity.QBoard.board;
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
}
