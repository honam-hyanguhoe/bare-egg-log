
package org.egglog.api.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.dto.params.*;
import org.egglog.api.board.model.dto.response.BoardListOutputSpec;
import org.egglog.api.board.model.dto.response.BoardModifyOutputSpec;
import org.egglog.api.board.model.dto.response.BoardOutputSpec;
import org.egglog.api.board.model.dto.response.BoardTypeListOutputSpec;
import org.egglog.api.board.model.service.BoardService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/hot")
    public ResponseEntity<MessageUtils<List<BoardListOutputSpec>>> getHotBoardList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getHotBoardList(user)));
    }


    @GetMapping("")
    public ResponseEntity<MessageUtils<List<BoardListOutputSpec>>> getBoardList(@RequestParam(value = "hospital_id", required = false) Long hospitalId, @RequestParam(value = "group_id", required = false) Long groupId,
                                          @RequestParam(value = "search_word", required = false) String searchWord, @RequestParam(value = "offset", required = false) Long offset,
                                          @AuthenticationPrincipal User user) {
        BoardListForm boardListForm = BoardListForm.builder()
                .hospitalId(hospitalId)
                .groupId(groupId)
                .searchWord(searchWord)
                .offset(offset)
                .build();

        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardList(boardListForm, user)));
    }

    @PostMapping("")
    public ResponseEntity<MessageUtils> registerBoard(@RequestBody @Valid BoardForm boardForm, @AuthenticationPrincipal User user) {
        boardService.registerBoard(boardForm, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<MessageUtils<BoardOutputSpec>> getBoard(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoard(boardId, user)));
    }

    @DeleteMapping("/{board_id}")
    public ResponseEntity<MessageUtils> deleteBoard(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        boardService.deleteBoard(boardId, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    @PatchMapping("/{board_id}")
    public ResponseEntity<MessageUtils<BoardModifyOutputSpec>> modifyBoard(@PathVariable("board_id") Long boardId, @RequestBody @Valid BoardUpdateForm boardUpdateForm, @AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(MessageUtils.success(boardService.modifyBoard(boardId, boardUpdateForm, user)));
    }

    //좋아요
    @PostMapping("/like")
    public ResponseEntity<MessageUtils> registerLike(@RequestBody @Valid LikeForm likeForm, @AuthenticationPrincipal User user) {
        boardService.registerLike(likeForm, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @DeleteMapping("/unlike/{board_id}")
    public ResponseEntity<MessageUtils> deleteLike(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        boardService.deleteLike(boardId, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    //사용자의 병원, 그룹 리스트
    @GetMapping("/types")
    public ResponseEntity<MessageUtils<BoardTypeListOutputSpec>> getBoardTypeListByUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardTypeListByUser(user)));
    }
}