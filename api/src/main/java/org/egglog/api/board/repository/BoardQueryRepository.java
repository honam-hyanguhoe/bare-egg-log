package org.egglog.api.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.model.entity.BoardHit;
import org.egglog.api.board.model.entity.BoardLike;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.board.model.entity.QBoard.board;
import static org.egglog.api.board.model.entity.QBoardHit.boardHit;
import static org.egglog.api.board.model.entity.QBoardLike.boardLike;

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

    public List<Board> findBoardHotList(Long groupId, Long hospitalId) {
        BooleanBuilder builder = new BooleanBuilder();

        return jpaQueryFactory
                .selectFrom(board)
                .where(
                        board.group.id.eq(groupId),
                        board.hospital.id.eq(hospitalId)
                )
                .orderBy(board.id.desc())
                .limit(2)
                .fetch();
    }

//    public List<Board> findBoardList(Long groupId, Long lastBoardId, int size) {
//        return jpaQueryFactory
//                .selectFrom(board)
//                .leftJoin(board)
//                .where(
//                        ltBoardId(lastBoardId),
//                        board.group.id.eq(groupId)
//                )
//                .orderBy(board.id.desc())
//                .limit(size)
//                .fetch();
//    }
//
//    // id < 첫 번째 조회에서는 파라미터를 사용하지 않기 위한 동적 쿼리
//    private BooleanExpression ltBoardId(Long lastBoardId) {
//        if (lastBoardId == null) {
//            return null;    // BooleanExpression 자리에 null이 반환되면 조건문에서 자동을 제외된다.
//        }
//        return board.id.lt(lastBoardId);
//    }


    /**
     * 조회수
     *
     * @param boardId
     * @return
     */
    public Long getHitCount(Long boardId) {
        Long count = jpaQueryFactory
                .select(boardHit.count())
                .from(boardHit)
                .where(boardHit.board.id.eq(boardId))
                .fetchOne();

        return count;
    }


    /**
     * 좋아요 개수
     *
     * @param boardId
     * @return
     */
    public Long getLikeCount(Long boardId) {
        Long count = jpaQueryFactory
                .select(boardLike.count())
                .from(boardLike)
                .where(boardLike.board.id.eq(boardId))
                .fetchOne();

        return count;
    }

    public Optional<BoardLike> getUserBoardLike(Long boardId, Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(boardLike)
                .where(
                        boardLike.user.id.eq(userId),
                        boardLike.board.id.eq(boardId)
                )
                .fetchOne());

    }

    public Optional<BoardHit> getUserBoardHit(Long boardId, Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(boardHit)
                .where(
                        boardHit.user.id.eq(userId),
                        boardHit.board.id.eq(boardId)
                )
                .fetchOne());

    }


}
