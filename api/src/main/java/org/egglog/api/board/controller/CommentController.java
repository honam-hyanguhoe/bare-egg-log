package org.egglog.api.board.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.dto.response.CommentListOutputSpec;
import org.egglog.api.board.model.service.CommentService;
import org.egglog.api.board.model.dto.params.CommentForm;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.board.controller
 * fileName      : CommentController
 * description    : 댓글관련 API
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-24|김도휘|최초 생성|
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/board")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 리스트 조회
     * @param boardId 게시판 ID
     * @return
     */
    @GetMapping("/{boardId}/comments")
    public ResponseEntity<MessageUtils<List<CommentListOutputSpec>>> getCommentList(
            @PathVariable Long boardId
    ) {
        return ResponseEntity.ok().body(MessageUtils.success(commentService.getCommentList(boardId)));
    }

    /**
     * 댓글 등록
     * @param commentForm boardId(필수), commentContent(필수), parentId(필수), commentDepth, tempNickname
     * @param user  로그인한 사용자 (JWT 토큰)
     * @return
     */
    @PostMapping("/comment")
    public ResponseEntity<MessageUtils<SuccessType>> registerComment(
            @RequestBody CommentForm commentForm,
            @AuthenticationPrincipal User user
    ) {
        commentService.registerComment(commentForm, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    /**
     * 댓글 삭제
     * @param commentId 삭제할 댓글 ID
     * @param user 로그인한 사용자 (JWT 토큰)
     * @return
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<MessageUtils<SuccessType>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user
    ) {
        commentService.deleteComment(commentId, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }
}