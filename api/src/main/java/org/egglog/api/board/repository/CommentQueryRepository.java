//package org.egglog.api.board.repository;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.egglog.api.board.model.entity.BoardLike;
//import org.egglog.api.board.model.entity.Comment;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.egglog.api.board.model.entity.QBoardHit.boardHit;
//import static org.egglog.api.board.model.entity.QBoardLike.boardLike;
//import static org.egglog.api.board.model.entity.QComment.comment;
//
//@Repository
//@RequiredArgsConstructor
//public class CommentQueryRepository {
//
//    private final JPAQueryFactory jpaQueryFactory;
//
//    public Long getCommentCount(Long boardId) {
//        Long count = jpaQueryFactory
//                .select(comment.count())
//                .from(comment)
//                .where(comment.board.id.eq(boardId))
//                .fetchOne();
//
//        return count;
//    }
//
//    public List<Comment> getCommentListByBoardId(Long boardId) {
//        return jpaQueryFactory
//                .selectFrom(comment)
//                .where(comment.board.id.eq(boardId))
//                .fetch();
//    }
//
//    public List<Comment> getRecommentListByCommentId(Long commentId) {
//        return jpaQueryFactory
//                .select(comment)
//                .where(comment.parentId.eq(commentId))
//                .fetch();
//
//    }
//}
