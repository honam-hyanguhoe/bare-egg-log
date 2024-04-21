package org.egglog.api.board.model.service;


import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.board.exception.BoardErrorCode;
import org.egglog.api.board.exception.BoardException;
import org.egglog.api.board.model.dto.params.*;
import org.egglog.api.board.model.dto.response.*;
import org.egglog.api.board.model.entity.*;
import org.egglog.api.board.repository.*;
import org.egglog.api.board.search.domain.document.BoardDocument;
import org.egglog.api.board.search.repository.SearchRepository;
import org.egglog.api.hospital.exception.HospitalErrorCode;
import org.egglog.api.hospital.exception.HospitalException;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.hospital.model.repository.HospitalQueryRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.Users;
import org.egglog.api.user.repository.UserQueryRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardJpaRepository boardJpaRepository;

    private final BoardQueryRepository boardQueryRepository;

    private final UserQueryRepository userQueryRepository;

    private final CommentQueryRepository commentQueryRepository;

    private final HospitalQueryRepository hospitalQueryRepository;

    private final BoardLikeJpaRepository boardLikeJpaRepository;

    private final BoardLikeQueryRepository boardLikeQueryRepository;

    private final BoardHitJpaRepository boardHitJpaRepository;

    private final SearchRepository searchRepository;    //검색 레포지토리

    /**
     * 게시판 글 목록
     *
     * @param boardListForm
     * @param userId
     * @return
     */
    @Override
    public List<BoardListOutputSpec> getBoardList(BoardListForm boardListForm, Long userId) {
        Users user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        List<Board> boardList = null;
        List<BoardListOutputSpec> boardListOutputSpecList = new ArrayList<>();
        int size = 10;

        try {
            List<BoardDocument> boardDocuments = searchRepository.searchBoard(boardListForm.getSearchWord(), boardListForm.getGroupId(), boardListForm.getHospitalId(), boardListForm.getLastBoardId(), size);

            for (BoardDocument board : boardDocuments) {
                Long commentCnt = commentQueryRepository.getCommentCount(board.getId());
                Long likeCnt = boardQueryRepository.getLikeCount(board.getId());
                Long hitCnt = boardQueryRepository.getHitCount(board.getId());
                boolean isUserLiked = false;  //좋아요 누른 여부
                boolean isUserHit = false;  //조회 여부

                //사용자가 이미 본 게시물인지
                if (!isNotHit(userId, board.getId())) { //아직 안봤다면 true
                    isUserHit = true;
                }
                //사용자가 이미 좋아요를 눌렀는지
                if (!isNotLiked(userId, board.getId())) { //아직 좋아요 안눌렀다면 true, 이미 좋아요 눌렀다면 false
                    isUserLiked = true;
                }
                BoardListOutputSpec boardListOutputSpec = BoardListOutputSpec.builder()
                        .boardId(board.getId())
                        .boardTitle(board.getTitle())
                        .boardContent(board.getContent())
                        .boardCreatedAt(board.getCreatedAt())
                        .hit(hitCnt)
                        .commentCount(commentCnt)
                        .boardLikeCount(likeCnt)
                        .doLiked(isUserLiked)  //좋아요 여부
                        .doHit(isUserHit)  //조회 여부
                        .build();

                boardListOutputSpecList.add(boardListOutputSpec);
            }

        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
        return boardListOutputSpecList;
    }

    /**
     * 급상승 게시물 조회
     *
     * @param hospitalId
     * @param groupId
     * @param userId
     * @return
     */
    public List<BoardListOutputSpec> getBoarHotList(Long hospitalId, Long groupId, Long userId) {

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

        Board board = Board.builder()
                .title(boardForm.getBoardTitle())
                .content(boardForm.getBoardContent())
                .pictureOne(boardForm.getPictureOne())
                .pictureTwo(boardForm.getPictureTwo())
                .pictureThree(boardForm.getPictureThree())
                .pictureFour(boardForm.getPictureFour())
                .user(user)
                .build();

        if (boardForm.getGroupId() == null && boardForm.getHospitalId() == null) {
            board.setBoardType(BoardType.ALL);

        } else if (boardForm.getGroupId() != null && boardForm.getHospitalId() == null) {
//            Group group = groupQueryRepository.findById(boardForm.getGroupId()).orElseThrow(
//                    () -> new GroupException(GroupErrorCode.NOT_FOUND)
//            );
            board.setBoardType(BoardType.GROUP);
//            board.setGroup(group);

        } else if (boardForm.getGroupId() == null && boardForm.getHospitalId() != null) {
            Hospital hospital = hospitalQueryRepository.findById(boardForm.getHospitalId()).orElseThrow(
                    () -> new HospitalException(HospitalErrorCode.NOT_FOUND)
            );
            board.setBoardType(BoardType.HOSPITAL);
            board.setHospital(hospital);

        } else {
            throw new BoardException(BoardErrorCode.NO_EXIST_CATEGORY);  //존재하지 않는 게시판 카테코리입니다.
        }

        try {
            boardJpaRepository.save(board);  //저장
            searchRepository.save(BoardDocument.from(board));   // ES에 저장

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
     * 사용자가 이미 좋아요를 눌렀는지
     *
     * @param userId
     * @param boardId
     * @return
     */
    private boolean isNotLiked(Long userId, Long boardId) {
        return boardQueryRepository.getUserBoardLike(boardId, userId).isEmpty();
    }

    /**
     * 사용자가 이미 본 게시물인지
     *
     * @param userId
     * @param boardId
     * @return
     */
    private boolean isNotHit(Long userId, Long boardId) {
        return boardQueryRepository.getUserBoardHit(boardId, userId).isEmpty();
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
        Users user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        BoardOutputSpec boardOutputSpec = null;
        BoardHit boardHit = BoardHit.builder()
                .id(boardId)
                .user(user)
                .build();
        try {
            boolean isUserLiked = false;  //좋아요 누른 여부
            boolean isUserHit = false;  //조회 여부

            //로그인한 유저가 아직 조회하지 않았다면
            if (isNotHit(userId, boardId)) {
                boardHitJpaRepository.save(boardHit);  //조회수 테이블에 저장
                isUserHit = true;
            }
            Board findBoard = boardQueryRepository.findById(boardId).orElseThrow(
                    () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
            );
            Long commentCnt = commentQueryRepository.getCommentCount(findBoard.getId());
            Long likeCnt = boardQueryRepository.getLikeCount(findBoard.getId());
            Long hitCnt = boardQueryRepository.getHitCount(findBoard.getId());

            //사용자가 이미 좋아요를 눌렀는지
            if (isNotLiked(userId, findBoard.getId())) {
                isUserLiked = true;
            }

            boardOutputSpec = BoardOutputSpec.builder()
                    .boardId(boardId)
                    .boardTitle(findBoard.getTitle())
                    .boardContent(findBoard.getContent())
                    .boardCreatedAt(findBoard.getCreatedAt())
                    .pictureOne(findBoard.getPictureOne())
                    .pictureTwo(findBoard.getPictureTwo())
                    .pictureThree(findBoard.getPictureThree())
                    .pictureFour(findBoard.getPictureFour())
                    .hit(hitCnt)
                    .groupId(findBoard.getGroup().getGroupId())
                    .userId(user.getId())
                    .userName(user.getUserName())
                    .profileImgUrl(user.getProfileImgUrl())
                    .commentCount(commentCnt)
                    .boardLikeCount(likeCnt)
                    .hospitalName(findBoard.getHospital().getHospitalName())
                    .doLiked(isUserLiked)
                    .doHit(isUserHit)
                    .build();

        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
        if (boardOutputSpec == null)
            throw new BoardException(BoardErrorCode.NO_EXIST_BOARD);

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
        Board board = boardQueryRepository.findById(likeForm.getBoardId()).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        try {
            //아직 좋아요 안눌렀다면
            if (isNotLiked(userId, likeForm.getBoardId())) {
                BoardLike boardLike = BoardLike.builder()
                        .id(likeForm.getBoardId())
                        .board(board)
                        .user(user)
                        .build();

                boardLikeJpaRepository.save(boardLike);
            }

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

        Board board = boardQueryRepository.findById(likeForm.getBoardId()).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        try {
            //이미 좋아요를 눌렀다면
            if (!isNotLiked(userId, likeForm.getBoardId())) {
                Optional<BoardLike> boardLike = boardLikeQueryRepository.getBoardLikeByBoardId(likeForm.getBoardId());
                boardLikeJpaRepository.delete(boardLike.get());
            }
        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }
    }


}
