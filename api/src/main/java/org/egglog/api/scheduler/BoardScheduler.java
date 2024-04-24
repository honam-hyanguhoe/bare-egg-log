package org.egglog.api.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.exception.BoardErrorCode;
import org.egglog.api.board.exception.BoardException;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.repository.BoardJpaRepository;
import org.egglog.api.board.repository.BoardQueryRepository;
import org.egglog.api.global.util.RedisViewCountUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class BoardScheduler {

    private final RedisViewCountUtil redisUtil;
    private final BoardQueryRepository boardQueryRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final StringRedisTemplate redisTemplate;  // String 데이터를 저장하기에 적합한 RedisTemplate


    /**
     * 조회수 자정에 DB 업데이트
     */
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void boardScheduledMethod() {
        if (!redisUtil.getAllViewedBoards().isEmpty()) {
            for (ZSetOperations.TypedTuple<String> tuple : redisUtil.getAllViewedBoards()) {
                Long boardId = Long.parseLong(Objects.requireNonNull(tuple.getValue())); //boardId
                Long score = Objects.requireNonNull(tuple.getScore()).longValue(); //조회수

                Board board = boardQueryRepository.findById(boardId).orElseThrow(
                        () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
                );
                Long viewCnt = board.getViewCount() + score;    // DB 조회수 + 하루 동안의 조회수
                board.setViewCount(viewCnt);    // DB에 저장
            }
        }
    }

    /**
     * 한시간마다 급상승 게시물 업데이트<br>
     * <br>
     * 전체게시판에만 급상승 게시물이 있음<br>
     * * 그룹아이디 && 병원아이디 == null -> 전체 게시판<br>
     */
    @Scheduled(cron = "0 0 0/1 * * *")
    @Transactional
    public void updateHotBoardScheduledMethod() {
        //<boardId, (조회수 + 좋아요)>
        Map<Long, Long> boardViewCounts = new HashMap<>();

        // 조회수 + 좋아요 상위 2개
        if (!redisUtil.getAllViewedBoards().isEmpty()) {
            for (ZSetOperations.TypedTuple<String> tuple : redisUtil.getAllViewedBoards()) {
                Long boardId = Long.parseLong(Objects.requireNonNull(tuple.getValue())); //boardId

                Board board = boardQueryRepository.findById(boardId).orElseThrow(
                        () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
                );
                //그룹 && 병원 == null -> 전체 게시판
                if (board.getGroup() == null && board.getHospital() == null) { //전체 게시판이라면
                    Long viewCount = Objects.requireNonNull(tuple.getScore()).longValue(); //조회수
                    Long likeCount = boardQueryRepository.getLikeCount(boardId); //좋아요 개수

                    boardViewCounts.put(boardId, viewCount + likeCount);
                }
            }
            // 합산된 점수 기준으로 상위 2개 게시물 ID만 추출
            List<Long> hotBoardIds = boardViewCounts.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .limit(2)
                    .toList();

            // Redis에 저장 (ID 리스트)
            redisTemplate.opsForValue().set("hotBoards", hotBoardIds.toString(), 1, TimeUnit.HOURS);

        } else {  //최근 게시물 2개
            List<Long> recentBoardIds = boardJpaRepository.findTop2ByOrderByCreatedAtDesc();
            redisTemplate.opsForValue().set("hotBoards", recentBoardIds.toString(), 1, TimeUnit.HOURS);

        }

    }

}