package org.egglog.api.board.controller;


import lombok.RequiredArgsConstructor;
import org.egglog.api.global.util.MessageUtils;
import org.egglog.api.board.model.dto.params.*;
import org.egglog.api.board.model.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/group/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/list")
    public ResponseEntity<?> getBoardList(@RequestBody BoardListForm boardListForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardList(boardListForm, userId)));
    }

    @PostMapping("/regist")
    public ResponseEntity<?> registBoard(@RequestBody BoardForm boardForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.registBoard(boardForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @PostMapping("/vote")
    public ResponseEntity<?> vote(@RequestBody VoteForm voteForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.vote(voteForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @DeleteMapping("/unvote")
    public ResponseEntity<?> unVote(@RequestBody VoteForm voteForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.unVote(voteForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardDataAll(boardId, userId)));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @PatchMapping("/modify")
    public ResponseEntity<?> modifyBoard(@RequestBody BoardModifyForm boardModifyForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.modifyBoard(boardModifyForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @GetMapping("/vote/user/{voteId}")
    public ResponseEntity<?> getVoteUser(@PathVariable Long voteId) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getVoteUser(voteId)));
    }

    @PostMapping("/like")
    public ResponseEntity<?> registLike(@RequestBody LikeForm likeForm) {
        //        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.registLike(likeForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<?> deleteLike(@RequestBody LikeForm likeForm) {
        //        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.deleteLike(likeForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }
}
