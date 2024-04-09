package org.egglog.api.groupboard.model.mapper;

import com.nursetest.app.groupboard.model.dto.params.BoardListForm;
import com.nursetest.app.groupboard.model.dto.response.*;
import com.nursetest.app.groupboard.model.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardListOutputSpec> getBoardList(BoardListForm boardListForm, Long userId);
    void registBoard(Board board);
    void modifyBoard(Board board);
    void registVote(Vote vote);
    void registVoteContent(VoteContent voteContent);
    void vote(VoteUser voteUser);
    void unVote(VoteUser voteUser);
    BoardOutputSpec getBoard(Long boardId, Long userId);
    VoteOutputSpec getVote(Long boardId, Long userId);
    List<VoteContentOutputSpec> getVoteContent(Long voteId, Long userId);
    void registHit(BoardHit boardHit);
    void deleteBoard(Long boardId, Long userId);
    List<VoteUserOutputSpec> getVoteUser(Long voteId);
    List<VoteUserInfoOutputSpec> getUserInfoByVoteContentId(Long voteContentId);
    void registLike(BoardLike boardLike);
    void deleteLike(BoardLike boardLike);
}
