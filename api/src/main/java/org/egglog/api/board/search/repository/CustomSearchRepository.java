package org.egglog.api.board.search.repository;

import org.egglog.api.board.search.domain.document.BoardDocument;

import java.util.List;

public interface CustomSearchRepository {
    List<BoardDocument> searchBoard(String keyword, Long groupId, Long hospitalId, Long lastBoardId, int size);
}
