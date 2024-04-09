package org.egglog.api.groupboard.model.service;

import com.nursetest.app.groupboard.exception.BoardErrorCode;
import com.nursetest.app.groupboard.exception.BoardException;
import com.nursetest.app.groupboard.model.dto.params.*;
import com.nursetest.app.groupboard.model.dto.response.*;
import com.nursetest.app.groupboard.model.entity.*;
import com.nursetest.app.groupboard.model.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardMapper boardMapper;

    /**
     * 그룹 게시판의 게시글을 가져오는 메서드<br/>
     * [로직]<br/>
     *      - boardListForm : 리스트의 시작 번호, 불러올 총 리스트 갯수, 검색 단어, 해당 그룹<br/>
     *      - userId: 내가 공감(좋아요)한 글인지 확인<br/>
     * @param boardListForm
     * @param userId
     * @return
     * @author 김수린
     */
    @Override
    public List<BoardListOutputSpec> getBoardList(BoardListForm boardListForm, Long userId) {
        List<BoardListOutputSpec> boardListOutputSpecList = null;
        try {
            boardListOutputSpecList = boardMapper.getBoardList(boardListForm, userId);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
        return boardListOutputSpecList;
    }

    /**
     * 게시판 작성을 위한 메서드<br/>
     * [로직]<br/>
     *      - boardForm에는 board 관련 정보를 담고 있는 객체와, 게시판 타입에 따라 투표/켈린더 정보를 담고 있는 객체 존재<br/>
     *      - 이를 분리해 BoardData를 Board객체로 변경<br/>
     *      - vote 타입이라면 vote 정보 insert 등록
     * @param boardForm
     * @param userId
     * @author 김수린
     */
    @Override
    @Transactional
    public void registBoard(BoardForm boardForm, Long userId) {
        BoardData boardData = boardForm.getBoardData();
        Board board = Board
                .builder()
                .boardSubject(boardData.getBoardSubject())
                .boardContent(boardData.getBoardContent())
                .groupId(boardData.getGroupId())
                .boardType(boardData.getBoardType())
                .pictureOne(boardData.getPictureOne())
                .pictureTwo(boardData.getPictureTwo())
                .pictureThree(boardData.getPictureThree())
                .pictureFour(boardData.getPictureFour())
                .userId(userId)
                .build();
        try {
            boardMapper.registBoard(board);

            if (boardData.getBoardType().equals("VOTE")) {
                VoteData voteData = boardForm.getVoteData();
                this.registVote(voteData, board.getBoardId());
            }
        } catch (PersistenceException e) {
            throw new BoardException(BoardErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 게시판 삭제 메서드<br/>
     * [로직]<br/>
     *      - 게시판 아이디와 현재 삭제 버튼을 클릭한 유저 아이디를 토대로 게시글을 삭제한다.
     * @param boardId
     * @param userId
     * @author 김수린
     */
    @Override
    public void deleteBoard(Long boardId, Long userId) {
        try {
            boardMapper.deleteBoard(boardId, userId);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 게시판 수정 메서드<br/>
     * [로직]<br/>
     *      - BoardModifyForm를 Board 객체로 변경<br/>
     *      - userId로 유효성 검증
     * @param boardModifyForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void modifyBoard(BoardModifyForm boardModifyForm, Long userId) {
        Board board = Board
                .builder()
                .boardId(boardModifyForm.getBoardId())
                .boardSubject(boardModifyForm.getBoardSubject())
                .boardContent(boardModifyForm.getBoardContent())
                .pictureOne(boardModifyForm.getPictureOne())
                .pictureTwo(boardModifyForm.getPictureTwo())
                .pictureThree(boardModifyForm.getPictureThree())
                .pictureFour(boardModifyForm.getPictureFour())
                .userId(userId)
                .build();
        try {
            boardMapper.modifyBoard(board);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 투표 삽입 메서드<br/>
     * [로직]<br/>
     *      - boardId 및 voteData로 Vote객체 변경<br/>
     *      - vote 삽입 후 voteId vote 객체에 저장(mybatis)<br/>
     *      - voteData객체 내의 content를 반복문으로 VoteContent 객체 생성<br/>
     *      - contents 삽입
     * @param voteData
     * @param boardId
     * @author 김수린
     */
    @Override
    @Transactional
    public void registVote(VoteData voteData, Long boardId) {
        Vote vote = Vote.builder()
                .boardId(boardId)
                .voteAnonymous(voteData.isVoteAnonymous())
                .voteName(voteData.getVoteName())
                .build();
        try {
            boardMapper.registVote(vote);
            for(String content : voteData.getVoteContent()) {
                VoteContent voteContent = VoteContent.builder()
                        .voteId(vote.getVoteId())
                        .voteContent(content)
                        .build();
                boardMapper.registVoteContent(voteContent);
            }
        } catch (PersistenceException e) {
            throw new BoardException(BoardErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * Board에 대한 상세정보를 가져오는 메서드<br/>
     * - Board 타입에 따른 추가 정보 존재 가능성 존재<br/>
     * [로직]<br/>
     *      - boardId를 토대로 boardData를 가져온다.<br/>
     *      - userId를 함께 넘겨 내가 좋아요한 글인지 등을 확인한다.<br/>
     *      - type이 vote라면 boardId를 토대로 vote 정보를 가져온다.<br/>
     *      - vote정보를 가져오며 voteId 역시 가져오고 이를 토대로 content를 가져온다.
     * @param boardId
     * @param userId
     * @return
     * @author 김수린
     */
    @Override
    @Transactional
    public Map<String, Object> getBoardDataAll(Long boardId, Long userId) {
        Map<String, Object> result = new HashMap<>();
        BoardOutputSpec boardOutputSpec = getBoard(boardId, userId);
        result.put("board_data", boardOutputSpec);
        if(boardOutputSpec.getBoardType().equals("VOTE")){
            VoteOutputSpec voteOutputSpec = getVote(boardId, userId);
            result.put("vote_data", voteOutputSpec);
            result.put("vote_content_data", getVoteContent(voteOutputSpec.getVoteId(), userId));
        }
        return result;
    }

    /**
     * Board 정보를 가져올 메서드<br/>
     * [로직]<br/>
     *      - 조회수를 먼저 높인다.<br/>
     *      - Board 정보를 가져온다.
     * @param boardId
     * @param userId
     * @return
     * @author 김수린
     */
    @Override
    public BoardOutputSpec getBoard(Long boardId, Long userId) {
        BoardOutputSpec boardOutputSpec = null;
        BoardHit boardHit = BoardHit.builder()
                .boardId(boardId)
                .userId(userId)
                .build();
        try {
            boardMapper.registHit(boardHit);
            boardOutputSpec = boardMapper.getBoard(boardId, userId);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
        if(boardOutputSpec == null) throw new BoardException(BoardErrorCode.NO_EXIST_BOARD);
        return boardOutputSpec;
    }

    /**
     * 투표 정보를 가져오는 메서드<br/>
     * [로직]<br/>
     *      - boardId를 토대로 투표정보를 가져온다.
     * @param boardId
     * @param userId
     * @return
     * @author 김수린
     */
    @Override
    public VoteOutputSpec getVote(Long boardId, Long userId) {
        VoteOutputSpec voteOutputSpec = null;
        try {
            voteOutputSpec = boardMapper.getVote(boardId, userId);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
        if(voteOutputSpec == null) throw new BoardException(BoardErrorCode.NO_EXIST_VOTE);
        return voteOutputSpec;
    }

    /**
     * 투표 세부 정보를 가져오는 메서드<br/>
     * [로직]<br/>
     *      - voteId를 토대로 투표 세부 정보를 가져온다.
     * @param voteId
     * @param userId
     * @return
     * @author 김수린
     */
    @Override
    public List<VoteContentOutputSpec> getVoteContent(Long voteId, Long userId) {
        List<VoteContentOutputSpec> voteContentOutputSpecList = null;
        try {
            voteContentOutputSpecList = boardMapper.getVoteContent(voteId, userId);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
        if(voteContentOutputSpecList == null || voteContentOutputSpecList.isEmpty()) throw new BoardException(BoardErrorCode.NO_EXIST_VOTE_CONTENT);
        return voteContentOutputSpecList;
    }

    /**
     * 투표하기 메서드<br/>
     * [로직]<br/>
     *      - VoteForm: voteId와 voteContentId를 담고 있다.<br/>
     *      - VoteForm를 VoteUser 객체로 변경.<br/>
     *      - 투표했음을 insert 한다.
     * @param voteForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void vote(VoteForm voteForm, Long userId) {
        VoteUser voteUser = VoteUser.builder()
                .userId(userId)
                .voteId(voteForm.getVoteId())
                .voteContentId(voteForm.getVoteContentId())
                .build();
        try {
            boardMapper.vote(voteUser);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 투표 취소 메서드<br/>
     * [로직]<br/>
     *      - VoteForm: voteId와 voteContentId를 담고 있다.<br/>
     *      - VoteForm를 VoteUser 객체로 변경.<br/>
     *      - 투표했던 것을 삭제한다.
     * @param voteForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void unVote(VoteForm voteForm, Long userId) {
        VoteUser voteUser = VoteUser.builder()
                .userId(userId)
                .voteId(voteForm.getVoteId())
                .voteContentId(voteForm.getVoteContentId())
                .build();
        try {
            boardMapper.unVote(voteUser);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 투표자 조회 메서드<br/>
     * [로직]
     *      - voteId를 가지고 현재 투표한 사람들의 명단을 가져온다.
     * @param voteId
     * @return
     * @author 김수린
     */
    @Override
    public List<VoteUserOutputSpec> getVoteUser(Long voteId) {
        List<VoteUserOutputSpec> voteUserOutputSpecList = null;
        try {
            voteUserOutputSpecList = boardMapper.getVoteUser(voteId);
            if(!voteUserOutputSpecList.isEmpty() && voteUserOutputSpecList != null) {
                for (VoteUserOutputSpec voteUserOutputSpec : voteUserOutputSpecList) {
                    List<VoteUserInfoOutputSpec> userInfoList = boardMapper.getUserInfoByVoteContentId(voteUserOutputSpec.getVoteContentId());
                    voteUserOutputSpec.setUserInfoList(userInfoList);
                }
            }
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
        if(voteUserOutputSpecList.isEmpty() || voteUserOutputSpecList == null) throw new BoardException(BoardErrorCode.NO_ANONYMOUS_VOTE_USER);
        return voteUserOutputSpecList;
    }

    /**
     * 공감(좋아요) 메서드<br/>
     * [로직]<br/>
     *      - LikeForm: 좋아요 하려는 boardId를 담고있음.<br/>
     *      - boardId를 가지고 공감을 시킨다.
     * @param likeForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void registLike(LikeForm likeForm, Long userId) {
        BoardLike boardLike = BoardLike.builder()
                .boardId(likeForm.getBoardId())
                .userId(userId)
                .build();
        try {
            boardMapper.registLike(boardLike);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 공감(좋아요) 취소 메서드<br/>
     * [로직]<br/>
     *      - LikeForm: 좋아요 취소 하려는 boardId를 담고있음.<br/>
     *      - boardId를 가지고 공감을 취소 시킨다.
     * @param likeForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void deleteLike(LikeForm likeForm, Long userId) {
        BoardLike boardLike = BoardLike.builder()
                .boardId(likeForm.getBoardId())
                .userId(userId)
                .build();
        try {
            boardMapper.deleteLike(boardLike);
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }
}
