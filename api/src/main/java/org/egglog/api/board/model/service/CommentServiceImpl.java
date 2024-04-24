package org.egglog.api.board.model.service;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.board.exception.BoardErrorCode;
import org.egglog.api.board.exception.BoardException;
import org.egglog.api.board.exception.CommentErrorCode;
import org.egglog.api.board.exception.CommentException;
import org.egglog.api.board.model.dto.params.CommentForm;
import org.egglog.api.board.model.dto.response.CommentListOutputSpec;
import org.egglog.api.board.model.dto.response.RecommentOutputSpec;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.model.entity.Comment;
import org.egglog.api.board.repository.BoardQueryRepository;
import org.egglog.api.board.repository.CommentJpaRepository;
import org.egglog.api.board.repository.CommentQueryRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.repository.UserQueryRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserQueryRepository userQueryRepository;

    private final BoardQueryRepository boardQueryRepository;

    private final CommentQueryRepository commentQueryRepository;

    private final CommentJpaRepository commentJpaRepository;

    /**
     * 댓글 조회 메서드<br/>
     * [로직]<br/>
     * - 댓글 리스트를 먼저 가져온다.(level이 0인 댓글)<br/>
     * - 대댓글 리스트를 댓글리스트를 통해 가져온다.
     *
     * @param boardId
     * @return
     */
    @Override
    @Transactional
    public List<CommentListOutputSpec> getCommentList(Long boardId) {
        Board board = boardQueryRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );
        List<CommentListOutputSpec> commentList = new ArrayList<>();

        try {
            List<Comment> comments = commentQueryRepository.getCommentListByBoardId(boardId);
            if (comments != null) {
                //댓글이 있다면
                for (Comment comment : comments) {
                    List<RecommentOutputSpec> recomments = new ArrayList<>();
                    List<Comment> recommentList = commentQueryRepository.getRecommentListByCommentId(comment.getId());

                    //대댓글이 있다면
                    if (recommentList != null) {
                        for (Comment recomment : recommentList) {
                            RecommentOutputSpec recommentListOutputSpec = RecommentOutputSpec.builder()
                                    .commentId(recomment.getId())
                                    .commentContent(recomment.getContent())
                                    .hospitalName(recomment.getUser().getHospital().getHospitalName())
                                    .userId(recomment.getUser().getId())
                                    .tempNickname(recomment.getTempNickname())
                                    .commentCreateAt(recomment.getCreatedAt())
                                    .profileImgUrl(recomment.getUser().getProfileImgUrl())
                                    .build();

                            recomments.add(recommentListOutputSpec);
                        }
                    }
                    CommentListOutputSpec commentListOutputSpec = CommentListOutputSpec.builder()
                            .commentId(comment.getId())
                            .commentContent(comment.getContent())
                            .hospitalName(comment.getUser().getHospital().getHospitalName())
                            .userId(comment.getUser().getId())
                            .tempNickname(comment.getTempNickname())
                            .commentCreateAt(comment.getCreatedAt())
                            .profileImgUrl(comment.getUser().getProfileImgUrl())
                            .recomments(recomments)
                            .build();

                    commentList.add(commentListOutputSpec);
                }
            }
        } catch (PersistenceException e) {
            throw new CommentException(CommentErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new CommentException(CommentErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommentException(CommentErrorCode.UNKNOWN_ERROR);
        }
        return commentList;
    }

    /**
     * 댓글 작성 메서드<br/>
     * [로직]<br/>
     * - commentForm: boardId, commentLevel, commentGroup, commentContent<br/>
     * - commentForm을 comment 객체로 변경<br/>
     * - 댓글 등록.
     *
     * @param commentForm
     * @param userId
     */
    @Override
    public void registComment(CommentForm commentForm, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        Board board = boardQueryRepository.findById(commentForm.getBoardId()).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        Comment comment = Comment.builder()
                .content(commentForm.getCommentContent())
                .parentId(commentForm.getParentId())
                .depth(commentForm.getCommentDepth())
                .tempNickname(commentForm.getTempNickname())
                .user(user)
                .board(board)
                .build();

        try {
            commentJpaRepository.save(comment);

        } catch (DataAccessException e) {
            throw new CommentException(CommentErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommentException(CommentErrorCode.UNKNOWN_ERROR);
        }
    }


    /**
     * 댓글 삭제 메서드<br/>
     * [로직]<br/>
     * - commentId를 토대로 댓글 삭제.
     *
     * @param commentId
     * @param userId
     */
    @Override
    public void deleteComment(Long commentId, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        Comment comment = commentJpaRepository.findById(commentId).orElseThrow(
                () -> new CommentException(CommentErrorCode.NO_EXIST_COMMENT)
        );
        try {
            //접속자와 작성자가 같다면 삭제 가능
            if (comment.getUser().getId().equals(userId)) {
                commentJpaRepository.delete(comment);
            }

        } catch (DataAccessException e) {
            throw new CommentException(CommentErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommentException(CommentErrorCode.UNKNOWN_ERROR);
        }
    }
}
