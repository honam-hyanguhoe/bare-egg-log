package org.egglog.api.board.repository.jpa.board;

import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.model.entity.BoardLike;

import java.util.List;
import java.util.Optional;

public interface BoardCustomQuery {

    List<Board> findBoardHotList(Long groupId, Long hospitalId);

    Long getLikeCount(Long boardId);

    Optional<BoardLike> getUserBoardLike(Long boardId, Long userId);

    List<Board> findBoardList(String keyword, Long groupId, Long hospitalId, Long lastBoardId, int size);

}
