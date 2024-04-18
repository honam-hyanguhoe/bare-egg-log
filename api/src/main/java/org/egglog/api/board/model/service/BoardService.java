package org.egglog.api.board.model.service;


import org.egglog.api.board.model.dto.params.*;
import org.egglog.api.board.model.dto.response.*;

import java.util.List;
import java.util.Map;

public interface BoardService {
    List<BoardListOutputSpec> getBoardList(BoardListForm boardListForm, Long userId);

    void registBoard(BoardForm boardForm, Long userId);

    void deleteBoard(Long boardId, Long userId);

    void modifyBoard(BoardModifyForm boardModifyForm, Long userId);

    Map<String, Object> getBoardDataAll(Long boardId, Long userId);

    BoardOutputSpec getBoard(Long boardId, Long userId);

    void registLike(LikeForm likeForm, Long userId);

    void deleteLike(LikeForm likeForm, Long userId);
}
