package org.egglog.api.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisViewCountUtil {

    private final RedisTemplate<String, String> redis;

    public boolean checkAndIncrementViewCount(String boardId, String userId) {
        String userKey = "view_count_number: " + boardId;
        Long addedCount = redis.opsForSet().add(userKey, userId); // 새로운 요소가 추가되면 1을 반환

        if (addedCount == 1L) {
            redis.expire(userKey, 24, TimeUnit.HOURS); // 만료 시간을 24시간으로 설정
            return true; // 조회 가능한 경우 조회수 증가
        }
        return false; // 이미 조회한 경우 조회수 증가하지 않음
    }

    public Set<String> getUsersByBoardId(String boardId) {
        return redis.opsForSet().members("view_count_number: " + boardId);
    }

    // Board 조회수 확인 *
    public Long getViewCount(String boardId) {
        Double viewCnt = redis.opsForZSet().score("view_count_board:", boardId);
        if (viewCnt == null) {
            return 0L; // 조회 수가 없는 경우 0을 반환
        } else {
            return viewCnt.longValue(); // Double 값을 Long으로 형변환
        }
    }

    // Board 조회수 증가 로직 *
    public void incrementViewCount(String boardId) {
        redis.opsForZSet().incrementScore("view_count_board:", boardId, 1);
    }

    public Set<ZSetOperations.TypedTuple<String>> getAllViewedBoards() {
        return redis.opsForZSet().reverseRangeWithScores("view_count_board:", 0, -1);
    }

    public void deleteViewCount(String boardId, String userId) {
        log.info("deleteViewCount 실행 {}", boardId);
        redis.opsForSet().remove("view_count_number: " + boardId, userId);
    }

    public void deleteViewCountBoard(String boardId) {
        redis.opsForZSet().remove("view_count_board:", boardId); // 조회수 점수 삭제
    }
}
