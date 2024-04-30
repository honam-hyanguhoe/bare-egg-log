
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
    public ResponseEntity<?> getHotBoardList(@RequestParam("hospital_id") Long hospitalId, @RequestParam("group_id") Long groupId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getHotBoardList(hospitalId, groupId, user.getId())));
    }


    @GetMapping("")
    public ResponseEntity<?> getBoardList(@RequestParam("hospital_id") Long hospitalId, @RequestParam("group_id") Long groupId,
                                          @RequestParam("search_word") String searchWord, @RequestParam("last_board_id") Long lastBoardId,
                                          @AuthenticationPrincipal User user) {
        BoardListForm boardListForm = BoardListForm.builder()
                .hospitalId(hospitalId)
                .groupId(groupId)
                .searchWord(searchWord)
                .lastBoardId(lastBoardId)
                .build();

        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardList(boardListForm, user.getId())));
    }

    @PostMapping("")
    public ResponseEntity<?> registerBoard(@RequestBody BoardForm boardForm, @AuthenticationPrincipal User user) {
        boardService.registerBoard(boardForm, user.getId());
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<?> getBoard(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoard(boardId, user.getId())));
    }

    @DeleteMapping("/{board_id}")
    public ResponseEntity<?> deleteBoard(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        boardService.deleteBoard(boardId, user.getId());
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    @PatchMapping("/{board_id}")
    public ResponseEntity<?> modifyBoard(@PathVariable("board_id") Long boardId, @RequestBody BoardUpdateForm boardUpdateForm, @AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(MessageUtils.success(boardService.modifyBoard(boardId, boardUpdateForm, user.getId())));
    }

    @PostMapping("/like")
    public ResponseEntity<?> registerLike(@RequestBody LikeForm likeForm, @AuthenticationPrincipal User user) {
        boardService.registerLike(likeForm, user.getId());
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @DeleteMapping("/unlike/{board_id}")
    public ResponseEntity<?> deleteLike(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        boardService.deleteLike(boardId, user.getId());
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }
}