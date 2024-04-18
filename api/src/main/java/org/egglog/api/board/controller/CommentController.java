//package org.egglog.api.board.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.egglog.api.global.util.MessageUtils;
//import org.egglog.api.board.model.dto.params.CommentForm;
//import org.egglog.api.board.model.dto.params.CommentModifyForm;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/v1/group/comment")
//public class CommentController {
//    private final CommentService commentService;
//
//    @GetMapping("/{boardId}")
//    public ResponseEntity<?> getCommentList(@PathVariable Long boardId) {
//        return ResponseEntity.ok().body(MessageUtils.success(commentService.getCommentList(boardId)));
//    }
//
//    @PostMapping("/regist")
//    public ResponseEntity<?> registComment(@RequestBody CommentForm commentForm) {
////        TODO @AuthenticationPrincipal User user
//        Long userId = 1L;
//        commentService.registComment(commentForm, userId);
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//    @PatchMapping("/modify")
//    public ResponseEntity<?> modifyComment(@RequestBody CommentModifyForm commentModifyForm) {
////        TODO @AuthenticationPrincipal User user
//        Long userId = 1L;
//        commentService.modifyComment(commentModifyForm, userId);
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//    @DeleteMapping("/{commentId}")
//    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
////        TODO @AuthenticationPrincipal User user
//        Long userId = 1L;
//        commentService.deleteComment(commentId, userId);
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//}
