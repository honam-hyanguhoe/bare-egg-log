package org.egglog.api.search.repository.elasticsearch;

import org.egglog.api.search.domain.document.BoardDocument;

import java.util.List;

public interface CustomSearchRepository {
    List<BoardDocument> searchBoard(String keyword, Long groupId, Long hospitalId, Long lastBoardId, int size);
}
