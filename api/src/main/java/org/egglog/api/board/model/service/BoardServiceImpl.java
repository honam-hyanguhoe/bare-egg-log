package org.egglog.api.board.model.service;


import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.board.exception.BoardErrorCode;
import org.egglog.api.board.exception.BoardException;
import org.egglog.api.board.model.dto.params.*;
import org.egglog.api.board.model.dto.response.*;
import org.egglog.api.board.model.entity.*;
import org.egglog.api.board.repository.BoardJpaRepository;
import org.egglog.api.board.repository.BoardQueryRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.Users;
import org.egglog.api.user.repository.UserQueryRepository;
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

    private final BoardJpaRepository boardJpaRepository;

    private final BoardQueryRepository boardQueryRepository;

    private final UserQueryRepository userQueryRepository;

    /**
     * 그룹 게시판의 게시글을 가져오는 메서드<br/>
     * [로직]<br/>
     * - boardListForm : 리스트의 시작 번호, 불러올 총 리스트 갯수, 검색 단어, 해당 그룹<br/>
     * - userId: 내가 공감(좋아요)한 글인지 확인<br/>
     *
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
     * - boardForm에는 board 관련 정보를 담고 있는 객체와, 게시판 타입에 따라 투표/켈린더 정보를 담고 있는 객체 존재<br/>
     * - 이를 분리해 BoardData를 Board객체로 변경<br/>
     * - vote 타입이라면 vote 정보 insert 등록
     *
     * @param boardForm
     * @param userId
     * @author 김수린
     */
    @Override
    @Transactional
    public void registBoard(BoardForm boardForm, Long userId) {
        Users user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        BoardData boardData = boardForm.getBoardData();
        Board board = Board
                .builder()
                .title(boardData.getBoardSubject())
                .content(boardData.getBoardContent())
                .id(boardData.getGroupId())
                .boardType(boardData.getBoardType())
                .pictureOne(boardData.getPictureOne())
                .pictureTwo(boardData.getPictureTwo())
                .pictureThree(boardData.getPictureThree())
                .pictureFour(boardData.getPictureFour())
                .user(user)
                .build();
        try {
            boardJpaRepository.save(board);

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
     * - 게시판 아이디와 현재 삭제 버튼을 클릭한 유저 아이디를 토대로 게시글을 삭제한다.
     *
     * @param boardId
     * @param userId
     * @author 김수린
     */
    @Override
    public void deleteBoard(Long boardId, Long userId) {
        Users user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Board board = boardQueryRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        try {
            boardJpaRepository.delete(board); //삭제

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
     * - BoardModifyForm를 Board 객체로 변경<br/>
     * - userId로 유효성 검증
     *
     * @param boardModifyForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void modifyBoard(BoardModifyForm boardModifyForm, Long userId) {
        Users user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        Board board = Board
                .builder()
                .id(boardModifyForm.getBoardId())
                .title(boardModifyForm.getBoardSubject())
                .content(boardModifyForm.getBoardContent())
                .pictureOne(boardModifyForm.getPictureOne())
                .pictureTwo(boardModifyForm.getPictureTwo())
                .pictureThree(boardModifyForm.getPictureThree())
                .pictureFour(boardModifyForm.getPictureFour())
                .user(user)
                .build();
        try {
            boardJpaRepository.save(board);

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
     * - boardId를 토대로 boardData를 가져온다.<br/>
     * - userId를 함께 넘겨 내가 좋아요한 글인지 등을 확인한다.<br/>
     * - type이 vote라면 boardId를 토대로 vote 정보를 가져온다.<br/>
     * - vote정보를 가져오며 voteId 역시 가져오고 이를 토대로 content를 가져온다.
     *
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
        if (boardOutputSpec.getBoardType().equals("VOTE")) {
            VoteOutputSpec voteOutputSpec = getVote(boardId, userId);
            result.put("vote_data", voteOutputSpec);
            result.put("vote_content_data", getVoteContent(voteOutputSpec.getVoteId(), userId));
        }
        return result;
    }

    /**
     * Board 정보를 가져올 메서드<br/>
     * [로직]<br/>
     * - 조회수를 먼저 높인다.<br/>
     * - Board 정보를 가져온다.
     *
     * @param boardId
     * @param userId
     * @return
     * @author 김수린
     */
    @Override
    public BoardOutputSpec getBoard(Long boardId, Long userId) {
        BoardOutputSpec boardOutputSpec = null;
        BoardHit boardHit = BoardHit.builder()
                .id(boardId)
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
        if (boardOutputSpec == null) throw new BoardException(BoardErrorCode.NO_EXIST_BOARD);
        return boardOutputSpec;
    }


    /**
     * 공감(좋아요) 메서드<br/>
     * [로직]<br/>
     * - LikeForm: 좋아요 하려는 boardId를 담고있음.<br/>
     * - boardId를 가지고 공감을 시킨다.
     *
     * @param likeForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void registLike(LikeForm likeForm, Long userId) {
        Users user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        BoardLike boardLike = BoardLike.builder()
                .id(likeForm.getBoardId())
                .user(user)
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
     * - LikeForm: 좋아요 취소 하려는 boardId를 담고있음.<br/>
     * - boardId를 가지고 공감을 취소 시킨다.
     *
     * @param likeForm
     * @param userId
     * @author 김수린
     */
    @Override
    public void deleteLike(LikeForm likeForm, Long userId) {
        Users user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        BoardLike boardLike = BoardLike.builder()
                .id(likeForm.getBoardId())
                .user(user)
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
