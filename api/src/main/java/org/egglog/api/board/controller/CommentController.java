package org.egglog.api.board.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.service.CommentService;
import org.egglog.api.board.model.dto.params.CommentForm;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/board")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{boardId}/comments")
    public ResponseEntity<?> getCommentList(@PathVariable Long boardId) {
        return ResponseEntity.ok().body(MessageUtils.success(commentService.getCommentList(boardId)));
    }

    @PostMapping("/comment")
    public ResponseEntity<?> registComment(@RequestBody CommentForm commentForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        commentService.registComment(commentForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }
}