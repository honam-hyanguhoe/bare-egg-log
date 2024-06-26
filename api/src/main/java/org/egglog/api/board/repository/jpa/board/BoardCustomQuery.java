package org.egglog.api.board.repository.jpa.board;

import org.egglog.api.board.model.dto.response.BoardListOutputSpec;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.model.entity.BoardLike;
import org.egglog.api.user.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface BoardCustomQuery {

    List<Board> findBoardHotList(Long groupId, Long hospitalId);

    Long getLikeCount(Long boardId);

    Optional<BoardLike> getUserBoardLike(Long boardId, Long userId);

    List<BoardListOutputSpec> findBoardList(String keyword, Long groupId, Long hospitalId, Long offset, int size, Long loginUserId);

    Optional<Board> findWithUserById(Long boardId);
}
