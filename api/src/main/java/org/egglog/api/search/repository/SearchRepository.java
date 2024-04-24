package org.egglog.api.search.repository;

import org.egglog.api.search.domain.document.BoardDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<BoardDocument, Long>, CustomSearchRepository {
}
