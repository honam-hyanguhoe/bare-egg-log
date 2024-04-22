package org.egglog.api.board.model.service;

import org.egglog.api.board.model.dto.params.BoardForm;
import org.egglog.api.board.model.dto.params.BoardListForm;
import org.egglog.api.board.model.dto.params.BoardModifyForm;
import org.egglog.api.board.model.dto.params.LikeForm;
import org.egglog.api.board.model.dto.response.BoardListOutputSpec;
import org.egglog.api.board.model.dto.response.BoardOutputSpec;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardService {

    List<BoardListOutputSpec> getBoardHotList(Long hospitalId, Long groupId, Long userId);

    List<BoardListOutputSpec> getBoardList(BoardListForm boardListForm, Long userId);

    void registBoard(BoardForm boardForm, Long userId);

    void deleteBoard(Long boardId, Long userId);

    void modifyBoard(BoardModifyForm boardModifyForm, Long userId);

    BoardOutputSpec getBoard(Long boardId, Long userId);

    void registLike(LikeForm likeForm, Long userId);

    void deleteLike(LikeForm likeForm, Long userId);
}
