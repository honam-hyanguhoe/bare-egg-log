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
import org.egglog.api.board.repository.jpa.board.BoardRepository;
import org.egglog.api.board.repository.jpa.comment.CommentRepository;
import org.egglog.api.notification.model.service.NotificationService;
import org.egglog.api.user.model.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    private final NotificationService notificationService;

    /**
     * 댓글 조회 메서드<br/>
     * [로직]<br/>
     * - 댓글 리스트를 먼저 가져온다.(level이 0인 댓글)<br/>
     * - 대댓글 리스트를 댓글리스트를 통해 가져온다.
     *
     * @param boardId
     * @return
     */
    @Transactional
    public List<CommentListOutputSpec> getCommentList(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );
        List<CommentListOutputSpec> commentList = new ArrayList<>();

        try {
            Optional<List<Comment>> commentResult = commentRepository.getCommentListByBoardId(boardId);
            if (commentResult.isPresent()) {
                //댓글이 있다면
                for (Comment comment : commentResult.get()) {
                    //부모 댓글(대댓글 X)
                    if (comment.getParentId() == 0) {
                        List<RecommentOutputSpec> recomments = new ArrayList<>();
                        Optional<List<Comment>> recommentResult = commentRepository.getRecommentListByCommentId(comment.getId());

                        //대댓글이 있다면
                        if (recommentResult.isPresent()) {
                            for (Comment recomment : recommentResult.get()) {
                                RecommentOutputSpec recommentListOutputSpec = RecommentOutputSpec.builder()
                                        .commentId(recomment.getId())
                                        .commentContent(recomment.getContent())
                                        .hospitalName(recomment.getUser().getSelectedHospital().getHospitalName())
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
                                .hospitalName(comment.getUser().getSelectedHospital().getHospitalName())
                                .userId(comment.getUser().getId())
                                .tempNickname(comment.getTempNickname())
                                .commentCreateAt(comment.getCreatedAt())
                                .profileImgUrl(comment.getUser().getProfileImgUrl())
                                .recomments(recomments)
                                .build();

                        commentList.add(commentListOutputSpec);
                    }
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
     * @param user
     */
    @Transactional
    public void registerComment(CommentForm commentForm, User user) {

        Board board = boardRepository.findWithUserById(commentForm.getBoardId()).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        Comment comment = Comment.builder()
                .content(commentForm.getCommentContent())
                .parentId(commentForm.getParentId())
                .tempNickname(commentForm.getTempNickname())
                .user(user)
                .board(board)
                .build();

        board.setIsCommented(true);     //댓글 생김

        try {
            Comment saveComment = commentRepository.save(comment);
            notificationService.registerCommentNotification(board, saveComment);
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
     * @param user
     */
    public void deleteComment(Long commentId, User user) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentException(CommentErrorCode.NO_EXIST_COMMENT)
        );
        try {
            //접속자와 작성자가 같다면 삭제 가능
            if (comment.getUser().getId().equals(user.getId())) {
                commentRepository.delete(comment);
                //해당 댓글에 달린 대댓글이 있다면 함께 삭제
                Optional<List<Comment>> recommentListByCommentId = commentRepository.getRecommentListByCommentId(commentId);
                if (recommentListByCommentId.isPresent()) {
                    commentRepository.deleteAll(recommentListByCommentId.get());
                }
            }

        } catch (DataAccessException e) {
            throw new CommentException(CommentErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommentException(CommentErrorCode.UNKNOWN_ERROR);
        }
    }
}