package org.egglog.api.board.repository.jpa.boardLike;

import org.egglog.api.board.model.entity.BoardLike;

import java.util.List;
import java.util.Optional;

public interface BoardLikeCustomQuery {
    Optional<BoardLike> getBoardLikeByBoardId(Long boardId);

    Optional<List<BoardLike>> getBoardLikeListByBoardId(Long boardId);
}
