package org.egglog.api.board.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RedisHash(value = "realTimeBoard", timeToLive = -1)
public class RealTimeBoard {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    @Indexed
    private Long boardId;

    public RealTimeBoard updateBoardId(Long id){
        this.boardId = id;
        return this;
    }
}
