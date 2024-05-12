
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
/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.board.controller
 * fileName      : BoardController
 * description    : 게시판 관련 API
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-24|김도휘|최초 생성|
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
public class BoardController {

    private final BoardService boardService;
    /**
     * 급상승 게시판 조회
     * @param user 로그인한 유저(JWT 토큰)
     * @return
     * @author 김도휘
     */
    @GetMapping("/hot")
    public ResponseEntity<MessageUtils<List<BoardListOutputSpec>>> getHotBoardList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getHotBoardList(user)));
    }

    /**
     * 게시판 리스트 조회
     * @param hospitalId 병원 게시판이면 병원ID
     * @param groupId 그룹 게시판이면 그룹 ID
     * @param searchWord 검색어
     * @param offset 몇개뽑을지
     * @param user 로그인한 유저
     * @return
     * @author 김도휘
     */
    @GetMapping("")
    public ResponseEntity<MessageUtils<List<BoardListOutputSpec>>> getBoardList(
            @RequestParam(value = "hospital_id", required = false) Long hospitalId,
            @RequestParam(value = "group_id", required = false) Long groupId,
            @RequestParam(value = "search_word", required = false) String searchWord,
            @RequestParam(value = "offset", required = false) Long offset,
            @AuthenticationPrincipal User user) {
        BoardListForm boardListForm = BoardListForm.builder()
                .hospitalId(hospitalId)
                .groupId(groupId)
                .searchWord(searchWord)
                .offset(offset)
                .build();

        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardList(boardListForm, user)));
    }
    /**
     * 게시판 등록
     * @param boardForm boardTitle(필수), boardContent(필수), pictureOne~pictureFour, tempNickname, groupId, hospitalId
     * @param user 로그인한 유저(jwt 토큰)
     * @return
     * @author 김도휘
     */
    @PostMapping("")
    public ResponseEntity<MessageUtils<SuccessType>> registerBoard(
            @RequestBody @Valid BoardForm boardForm,
            @AuthenticationPrincipal User user
    ) {
        boardService.registerBoard(boardForm, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }
    /**
     * 게시판 단건 조회
     * @param boardId 조회할 게시판 ID
     * @param user 로그인한 유저(jwt 토큰)
     * @return
     * @author 김도휘
     */
    @GetMapping("/{board_id}")
    public ResponseEntity<MessageUtils<BoardOutputSpec>> getBoard(
            @PathVariable("board_id") Long boardId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoard(boardId, user)));
    }
    /**
     * 게시판 삭제
     * @param boardId 삭제할 게시판 ID
     * @param user 로그인한 유저(jwt 토큰)
     * @return
     * @author 김도휘
     */
    @DeleteMapping("/{board_id}")
    public ResponseEntity<MessageUtils<SuccessType>> deleteBoard(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        boardService.deleteBoard(boardId, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }
    /**
     * 게시판 수정
     * @param boardId 수정할 게시판 ID
     * @param boardUpdateForm boardTitle(필수), boardContent(필수), pictureOne~pictureFour
     * @param user 로그인한 유저(jwt 토큰)
     * @return
     * @author 김도휘
     */
    @PatchMapping("/{board_id}")
    public ResponseEntity<MessageUtils<BoardModifyOutputSpec>> modifyBoard(
            @PathVariable("board_id") Long boardId,
            @RequestBody @Valid BoardUpdateForm boardUpdateForm,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok().body(MessageUtils.success(
                boardService.modifyBoard(boardId, boardUpdateForm, user)));
    }

    //좋아요
    /**
     * 좋아요 등록
     * @param likeForm boardId 좋아할 게시판 ID
     * @param user 로그인한 유저(jwt 토큰)
     * @return
     * @author 김도휘
     */
    @PostMapping("/like")
    public ResponseEntity<MessageUtils<SuccessType>> registerLike(@RequestBody @Valid LikeForm likeForm, @AuthenticationPrincipal User user) {
        boardService.registerLike(likeForm, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    /**
     * 좋아요 삭제
     * @param boardId boardId 좋아하지 않을 게시판 ID
     * @param user 로그인한 유저(jwt 토큰)
     * @return
     * @author 김도휘
     */
    @DeleteMapping("/unlike/{board_id}")
    public ResponseEntity<MessageUtils<SuccessType>> deleteLike(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal User user) {
        boardService.deleteLike(boardId, user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    //사용자의 병원, 그룹 리스트
    /**
     * 사용자의 병원, 그룹 리스트
     * @param user 로그인한 유저(jwt 토큰)
     * @return
     * @author 김도휘
     */
    @GetMapping("/types")
    public ResponseEntity<MessageUtils<BoardTypeListOutputSpec>> getBoardTypeListByUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(MessageUtils.success(boardService.getBoardTypeListByUser(user)));
    }
}