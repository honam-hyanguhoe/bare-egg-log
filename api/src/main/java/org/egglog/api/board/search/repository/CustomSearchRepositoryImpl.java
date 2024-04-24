package org.egglog.api.board.search.repository;

import lombok.RequiredArgsConstructor;
import org.egglog.api.board.search.domain.document.BoardDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomSearchRepositoryImpl implements CustomSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<BoardDocument> searchBoard(String keyword, Long groupId, Long hospitalId, Long lastBoardId, int size) {
        Criteria criteria = new Criteria();

        // 마지막 게시물 ID보다 작은 경우
        if (lastBoardId != null) {
            criteria.and(new Criteria("id").lessThan(lastBoardId));
        }

        // 그룹 ID가 주어진 경우 -> 그룹 게시판
        if (groupId != null) {
            criteria.and(new Criteria("groupId").is(groupId));
        }

        // 병원 ID가 주어진 경우 -> 병원 게시판
        if (hospitalId != null) {
            criteria.and(new Criteria("hospitalId").is(hospitalId));
        }
        Criteria keywordCriteria = null;

        // 키워드가 제목 또는 내용에 있는 경우
        if (keyword != null) {
            keywordCriteria = new Criteria("title").contains(keyword)
                    .or(new Criteria("content").contains(keyword));

            criteria.subCriteria(keywordCriteria);
        }

        Query searchQuery = new CriteriaQuery(criteria)
                .addSort(Sort.by(Sort.Direction.DESC, "id"))  // id를 역순으로 정렬
                .setPageable(Pageable.ofSize(size)); // 페이지네이션 및 정렬 설정

        SearchHits<BoardDocument> searchHits = elasticsearchOperations.search(searchQuery, BoardDocument.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

}