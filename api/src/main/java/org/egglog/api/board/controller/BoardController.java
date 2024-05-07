
package org.egglog.api.board.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.dto.params.*;
import org.egglog.api.board.model.service.BoardService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/hot")
    public ResponseEntity<?> getHotBoardList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getHotBoardList(user)));
    }


    @GetMapping("")
    public ResponseEntity<?> getBoardList(@RequestParam(value = "hospital_id", required = false) Long hospitalId, @RequestParam(value = "group_id", required = false) Long groupId,
                                          @RequestParam(value = "search_word", required = false) String searchWord, @RequestParam(value = "last_board_id", required = false) Long lastBoardId,
                                          @AuthenticationPrincipal User user) {
        BoardListForm boardListForm = BoardListForm.builder()
                .hospitalId(hospitalId)
                .groupId(groupId)
                .searchWord(searchWord)
                .lastBoardId(lastBoardId)
                .build();

        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardList(boardListForm, user)));
    }

    @PostMapping("")
    public ResponseEntity<?> registerBoard(@RequestBody BoardForm boardForm, @AuthenticationPrincipal User user) {
        boardService.registerBoard(boardForm, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<?> getBoard(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoard(boardId, user)));
    }

    @DeleteMapping("/{board_id}")
    public ResponseEntity<?> deleteBoard(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        boardService.deleteBoard(boardId, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    @PatchMapping("/{board_id}")
    public ResponseEntity<?> modifyBoard(@PathVariable("board_id") Long boardId, @RequestBody BoardUpdateForm boardUpdateForm, @AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(MessageUtils.success(boardService.modifyBoard(boardId, boardUpdateForm, user)));
    }

    //좋아요
    @PostMapping("/like")
    public ResponseEntity<?> registerLike(@RequestBody LikeForm likeForm, @AuthenticationPrincipal User user) {
        boardService.registerLike(likeForm, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @DeleteMapping("/unlike/{board_id}")
    public ResponseEntity<?> deleteLike(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        boardService.deleteLike(boardId, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    //사용자의 병원, 그룹 리스트
    @GetMapping("/types")
    public ResponseEntity<?> getBoardTypeListByUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardTypeListByUser(user)));
    }
}