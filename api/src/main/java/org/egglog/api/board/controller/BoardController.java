
package org.egglog.api.board.controller;

import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.dto.params.*;
import org.egglog.api.board.model.service.BoardService;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/hot")
    public ResponseEntity<?> getBoardHotList(@RequestParam("hospital_id") Long hospitalId, @RequestParam("group_id") Long groupId) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardHotList(hospitalId, groupId, userId)));
    }


    @GetMapping("")
    public ResponseEntity<?> getBoardList(@RequestParam("hospital_id") Long hospitalId, @RequestParam("group_id") Long groupId,
                                          @RequestParam("search_word") String searchWord, @RequestParam("last_board_id") Long lastBoardId) {
//        TODO @AuthenticationPrincipal User user
        BoardListForm boardListForm = BoardListForm.builder()
                .hospitalId(hospitalId)
                .groupId(groupId)
                .searchWord(searchWord)
                .lastBoardId(lastBoardId)
                .build();

        Long userId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardList(boardListForm, userId)));
    }

    @PostMapping("")
    public ResponseEntity<?> registBoard(@RequestBody BoardForm boardForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.registBoard(boardForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoard(boardId, userId)));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @PatchMapping("")
    public ResponseEntity<?> modifyBoard(@RequestBody BoardModifyForm boardModifyForm) {
//        TODO @AuthenticationPrincipal User user
        Long userId = 1L;
        boardService.modifyBoard(boardModifyForm, userId);
        return ResponseEntity.ok().body(MessageUtils.success());
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