package org.egglog.api.board.repository.jpa.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.board.model.dto.response.BoardListOutputSpec;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.model.entity.BoardLike;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.egglog.api.board.model.entity.QBoard.board;
import static org.egglog.api.board.model.entity.QBoardLike.boardLike;
import static org.egglog.api.board.model.entity.QComment.comment;
import static org.egglog.api.hospital.model.entity.QHospital.hospital;
import static org.egglog.api.hospital.model.entity.QHospitalAuth.hospitalAuth;

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
@Slf4j
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

    @Override
    public List<BoardListOutputSpec> findBoardList(String keyword, Long groupId, Long hospitalId, Long offset, int size, Long loginUserId) {
        BooleanExpression whereClause = board.isNotNull()
                .and(groupId != null ? board.group.id.eq(groupId) : board.group.id.isNull())
                .and(hospitalId != null ? board.hospital.id.eq(hospitalId) : board.hospital.id.isNull())
                .and(keyword != null && !keyword.isEmpty() ? searchKeyword(keyword) : null);

        List<Tuple> results = jpaQueryFactory
                .select(
                        board,
                        comment.countDistinct(),
                        boardLike.countDistinct(),
                        JPAExpressions.select(board.user.selectedHospital.hospitalName)
                                .from(hospital)
                                .where(hospital.id.eq(board.user.selectedHospital.id)),
                        JPAExpressions.select(hospitalAuth.auth)
                                .from(hospitalAuth)
                                .where(
                                        hospitalAuth.user.eq(board.user),
                                        hospitalAuth.hospital.eq(board.user.selectedHospital)
                                ),
                        JPAExpressions.select(boardLike)
                                .from(boardLike)
                                .where(
                                        boardLike.user.id.eq(loginUserId),
                                        boardLike.board.eq(board)
                                )
                )
                .from(board)
                .leftJoin(comment).on(comment.board.eq(board))
                .leftJoin(boardLike).on(boardLike.board.eq(board))
                .where(whereClause)
                .groupBy(board.id)
                .orderBy(board.id.desc())
                .offset(offset)
                .limit(size)
                .fetch();

        return results.stream().map(tuple -> new BoardListOutputSpec(
                tuple.get(board),
                tuple.get(comment.countDistinct().longValue()),
                tuple.get(boardLike.countDistinct().longValue()),
                tuple.get(3, String.class),
                tuple.get(4, Boolean.class) != null ? tuple.get(4, Boolean.class) : false,
                tuple.get(5, BoardLike.class) != null ? true : false
        )).collect(Collectors.toList());

    }

    private BooleanExpression searchKeyword(String keyword) {
        return Expressions.numberTemplate(Double.class,
                        "function('match_against', {0}, {1}, {2})", board.title, board.content, "+" + keyword + "*")
                .gt(0);

    }

    public Optional<Board> findWithUserById(Long boardId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(board)
                .leftJoin(board.user).fetchJoin()
                .where(board.id.eq(boardId))
                .fetchOne());
    }

}