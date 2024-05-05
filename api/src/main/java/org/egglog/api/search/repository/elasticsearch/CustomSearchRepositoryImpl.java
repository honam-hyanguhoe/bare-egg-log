package org.egglog.api.search.repository.elasticsearch;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.model.entity.BoardType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import static org.egglog.api.board.model.entity.QBoard.board;

@Repository
@RequiredArgsConstructor
public class CustomSearchRepositoryImpl implements CustomSearchRepository {

//    private final JdbcTemplate jdbcTemplate;

    private final JPAQueryFactory jpaQueryFactory;

//    private final ElasticsearchOperations elasticsearchOperations;
//
//    public CustomSearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
//        this.elasticsearchOperations = elasticsearchOperations;
//    }
//
//    @Override
//    public List<BoardDocument> searchBoard(String keyword, Long groupId, Long hospitalId, Long lastBoardId, int size) {
//        Criteria criteria = new Criteria();
//
//        // 마지막 게시물 ID보다 작은 경우
//        if (lastBoardId != null) {
//            criteria.and(new Criteria("id").lessThan(lastBoardId));
//        }
//
//        // 그룹 ID가 주어진 경우 -> 그룹 게시판
//        if (groupId != null) {
//            criteria.and(new Criteria("groupId").is(groupId));
//        }
//
//        // 병원 ID가 주어진 경우 -> 병원 게시판
//        if (hospitalId != null) {
//            criteria.and(new Criteria("hospitalId").is(hospitalId));
//        }
//        Criteria keywordCriteria = null;
//
//        // 키워드가 제목 또는 내용에 있는 경우
//        if (keyword != null) {
//            keywordCriteria = new Criteria("title").contains(keyword)
//                    .or(new Criteria("content").contains(keyword));
//
//            criteria.subCriteria(keywordCriteria);
//        }
//
//        Query searchQuery = new CriteriaQuery(criteria)
//                .addSort(Sort.by(Sort.Direction.DESC, "id"))  // id를 역순으로 정렬
//                .setPageable(Pageable.ofSize(size)); // 페이지네이션 및 정렬 설정
//
//        SearchHits<BoardDocument> searchHits = elasticsearchOperations.search(searchQuery, BoardDocument.class);
//
//        return searchHits.stream()
//                .map(SearchHit::getContent)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<Board> searchFullText(String keyword, Long groupId, Long hospitalId, Long lastBoardId, int size) {
//        sql = "SELECT * FROM Board b " +
//                "WHERE MATCH (b.board_title, b.board_content) AGAINST (? IN BOOLEAN MODE) " +
//                "AND b.group_id = ? AND b.hospital_id = ? AND  AND b.board_id < ? " +
//                "ORDER BY b.board_id DESC LIMIT ?";
//
//        return jdbcTemplate.query(sql, this.mapBoard(), keyword, groupId, hospitalId, lastBoardId, size);

        BooleanExpression whereClause = board.isNotNull(); // 기본 조건

        // 마지막 게시물 ID보다 작은 경우
        if (lastBoardId != null) {
            whereClause = whereClause.and(board.group.id.eq(groupId));
        }

        // 그룹 ID가 주어진 경우 -> 그룹 게시판
        if (groupId != null) {
            whereClause = whereClause.and(board.hospital.id.eq(hospitalId));
        }

        // 병원 ID가 주어진 경우 -> 병원 게시판
        if (hospitalId != null) {
            whereClause = whereClause.and(board.id.lt(lastBoardId));
        }

        // 키워드가 제목 또는 내용에 있는 경우
        if (keyword != null) {
            whereClause = whereClause.and(searchKeyword(keyword));
        }

         List<Board> boards = jpaQueryFactory
                .selectFrom(board)
                .where(whereClause)
                .orderBy(board.id.desc())
                .limit(size)
                .fetch();

         return boards;
    }

    private BooleanExpression searchKeyword(String keyword) {
        return Expressions.numberTemplate(Double.class,
                        "function('match_against', {0}, {1}, {2})", board.title, board.content, "+" + keyword + "*")
                .gt(0);

    }

    //jdbcTemplate 사용할 때 필요한 메소드
    private RowMapper<Board> mapBoard() {
        return (rs, rowNum) -> {
            Board board = new Board();
            board.setId(rs.getLong("board_id"));
            board.setTitle(rs.getString("board_title"));
            board.setContent(rs.getString("board_content"));
            board.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
            board.setTempNickname(rs.getString("temp_nickname"));
            board.setViewCount(rs.getLong("view_count"));
            board.setIsCommented(rs.getBoolean("is_commented"));
            board.setPictureOne(rs.getString("picture_one"));
            board.setPictureTwo(rs.getString("picture_two"));
            board.setPictureThree(rs.getString("picture_three"));
            board.setPictureFour(rs.getString("picture_four"));
            board.setBoardType(BoardType.valueOf(rs.getString("boardType")));

            return board;
        };
    }


}
