package org.egglog.api.groupboard.model.service;

import com.nursetest.app.groupboard.exception.CommentErrorCode;
import com.nursetest.app.groupboard.exception.CommentException;
import com.nursetest.app.groupboard.model.dto.params.CommentForm;
import com.nursetest.app.groupboard.model.dto.params.CommentModifyForm;
import com.nursetest.app.groupboard.model.dto.response.CommentListOutputSpec;
import com.nursetest.app.groupboard.model.dto.response.RecommentOutputSpec;
import com.nursetest.app.groupboard.model.entity.Comment;
import com.nursetest.app.groupboard.model.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;

    /**
     * 댓글 조회 메서드<br/>
     * [로직]<br/>
     *      - 댓글 리스트를 먼저 가져온다.(level이 0인 댓글)<br/>
     *      - 대댓글 리스트를 댓글리스트를 통해 가져온다.
     * @param boardId
     * @return
     * @author 김수린
     */
    @Override
    @Transactional
    public List<CommentListOutputSpec> getCommentList(Long boardId) {
        List<CommentListOutputSpec> commentList = null;
        try {
            commentList = commentMapper.getCommentList(boardId);
            if(!commentList.isEmpty() && commentList != null) {
                for (CommentListOutputSpec comment : commentList) {
                    List<RecommentOutputSpec> recomment = commentMapper.getRecommentsByCommentId(comment.getCommentId());
                    comment.setRecomment(recomment);
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
     *      - commentForm: boardId, commentLevel, commentGroup, commentContent<br/>
     *      - commentForm을 comment 객체로 변경<br/>
     *      - 댓글 등록.
     * @param commentForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void registComment(CommentForm commentForm, Long userId) {
        Comment comment = Comment.builder()
                .boardId(commentForm.getBoardId())
                .commentLevel(commentForm.getCommentLevel())
                .commentGroup(commentForm.getCommentGroup())
                .commentContent(commentForm.getCommentContent())
                .userId(userId)
                .build();
        try {
            commentMapper.registComment(comment);
        } catch (DataAccessException e) {
            throw new CommentException(CommentErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommentException(CommentErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 댓글 수정 메서드<br/>
     * [로직]<br/>
     *      - commentModifyForm: commentContent, commentId<br/>
     *      - commentForm을 comment 객체로 변경<br/>
     *      - 댓글 수정.
     * @param commentModifyForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void modifyComment(CommentModifyForm commentModifyForm, Long userId) {
        Comment comment = Comment.builder()
                .commentContent(commentModifyForm.getCommentContent())
                .commentId(commentModifyForm.getCommentId())
                .userId(userId)
                .build();
        try {
            commentMapper.modifyComment(comment);
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
     *      - commentId를 토대로 댓글 삭제.
     * @param commentId
     * @param userId
     * @author 김수린
     */
    @Override
    public void deleteComment(Long commentId, Long userId) {
        try {
            commentMapper.deleteComment(commentId, userId);
        } catch (DataAccessException e) {
            throw new CommentException(CommentErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommentException(CommentErrorCode.UNKNOWN_ERROR);
        }
    }
}
