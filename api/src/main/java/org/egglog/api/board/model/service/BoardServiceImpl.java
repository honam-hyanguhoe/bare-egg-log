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
import org.egglog.api.search.domain.document.BoardDocument;
import org.egglog.api.search.repository.SearchRepository;
import org.egglog.api.global.util.RedisViewCountUtil;
import org.egglog.api.hospital.exception.HospitalErrorCode;
import org.egglog.api.hospital.exception.HospitalException;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.hospital.repository.HospitalQueryRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.UserQueryRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    //사용자
    private final UserQueryRepository userQueryRepository;

    //게시판
    private final BoardJpaRepository boardJpaRepository;
    private final BoardQueryRepository boardQueryRepository;

    //좋아요
    private final BoardLikeJpaRepository boardLikeJpaRepository;
    private final BoardLikeQueryRepository boardLikeQueryRepository;

    //댓글
    private final CommentQueryRepository commentQueryRepository;
    private final CommentJpaRepository commentJpaRepository;

    //병원
    private final HospitalQueryRepository hospitalQueryRepository;

    //검색
    private final SearchRepository searchRepository;

    private final CommentService commentService;

    private final RedisViewCountUtil redisViewCountUtil;    //조회수

    private final StringRedisTemplate redisTemplate;    //급상승 게시물

    /**
     * 게시판 글 목록
     *
     * @param boardListForm
     * @param userId
     * @return
     */
    @Override
    public List<BoardListOutputSpec> getBoardList(BoardListForm boardListForm, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        List<BoardListOutputSpec> boardListOutputSpecList = new ArrayList<>();
        int size = 10;

        try {
            List<BoardDocument> boardDocuments = searchRepository.searchBoard(boardListForm.getSearchWord(), boardListForm.getGroupId(), boardListForm.getHospitalId(), boardListForm.getLastBoardId(), size);

            for (BoardDocument board : boardDocuments) {
                Long commentCnt = commentQueryRepository.getCommentCount(board.getId());
                Long likeCnt = boardQueryRepository.getLikeCount(board.getId());
                long viewCount = redisViewCountUtil.getViewCount(String.valueOf(board.getId())); //하루 동안의 조회수
                Long hitCnt = viewCount + board.getViewCount();

                boolean isUserLiked = false;  //좋아요 누른 여부

                //사용자가 이미 좋아요를 눌렀는지
                if (!isNotLiked(userId, board.getId())) { //아직 좋아요 안눌렀다면 true, 이미 좋아요 눌렀다면 false
                    isUserLiked = true;
                }
                BoardListOutputSpec boardListOutputSpec = BoardListOutputSpec.builder()
                        .boardId(board.getId())
                        .boardTitle(board.getTitle())
                        .boardContent(board.getContent())
                        .boardCreatedAt(board.getCreatedAt())
                        .tempNickname(board.getTempNickname())
                        .viewCount(hitCnt)
                        .commentCount(commentCnt)
                        .likeCount(likeCnt)
                        .doLiked(isUserLiked)  //좋아요 여부
//                        .isAuth(user.getIsAuth())   //인증 여부
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
    public List<BoardListOutputSpec> getHotBoardList(Long hospitalId, Long groupId, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        List<BoardListOutputSpec> boardListOutputSpecList = new ArrayList<>();

        try {
            String boardIds = redisTemplate.opsForValue().get("hotBoards");
            if (boardIds != null && !boardIds.isEmpty()) {
                List<Long> ids = Arrays.stream(boardIds.replace("[", "").replace("]", "").split(","))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .toList();

                for (Long boardId : ids) {
                    Board board = boardQueryRepository.findById(boardId).orElseThrow(
                            () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
                    );

                    Long commentCnt = commentQueryRepository.getCommentCount(board.getId());
                    Long likeCnt = boardQueryRepository.getLikeCount(board.getId());
                    long viewCount = redisViewCountUtil.getViewCount(String.valueOf(boardId)); //하루 동안의 조회수
                    Long hitCnt = viewCount + board.getViewCount();

                    boolean isUserLiked = false;  //좋아요 누른 여부

                    //사용자가 이미 좋아요를 눌렀는지
                    if (!isNotLiked(userId, board.getId())) { //아직 좋아요 안눌렀다면 true, 이미 좋아요 눌렀다면 false
                        isUserLiked = true;
                    }

                    BoardListOutputSpec boardListOutputSpec = BoardListOutputSpec.builder()
                        .boardId(board.getId())
                        .boardTitle(board.getTitle())
                        .boardContent(board.getContent())
                        .boardCreatedAt(board.getCreatedAt())
                        .tempNickname(board.getTempNickname())
                        .viewCount(hitCnt)
                        .commentCount(commentCnt)
                        .likeCount(likeCnt)
                        .doLiked(isUserLiked)
//                        .isAuth(user.getIsAuth())   //인증 여부
                        .build();

                    boardListOutputSpecList.add(boardListOutputSpec);
                }
            }

        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }

        return boardListOutputSpecList;

    }

//    public List<BoardListOutputSpec> getBoardHotList(Long hospitalId, Long groupId, Long userId) {
//        Users user = userQueryRepository.findById(userId).orElseThrow(
//                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
//        );
//
//        List<BoardListOutputSpec> boardListOutputSpecList = new ArrayList<>();
//
//        try {
//            List<Board> boardHotList = boardQueryRepository.findBoardHotList(groupId, hospitalId);
//
//            for (Board board : boardHotList) {
//                Long commentCnt = commentQueryRepository.getCommentCount(board.getId());
//                Long likeCnt = boardQueryRepository.getLikeCount(board.getId());
//                long viewCount = redisViewCountUtil.getViewCount(String.valueOf(board.getId())); //하루 동안의 조회수
//                Long hitCnt = viewCount + board.getViewCount();     // DB + redis
//
//                boolean isUserLiked = false;  //좋아요 누른 여부
//                boolean isUserHit = false;  //조회 여부
//
//                //사용자가 이미 좋아요를 눌렀는지
//                if (!isNotLiked(userId, board.getId())) { //아직 좋아요 안눌렀다면 true, 이미 좋아요 눌렀다면 false
//                    isUserLiked = true;
//                }
//
//                BoardListOutputSpec boardListOutputSpec = BoardListOutputSpec.builder()
//                        .boardId(board.getId())
//                        .boardTitle(board.getTitle())
//                        .boardContent(board.getContent())
//                        .boardCreatedAt(board.getCreatedAt())
//                        .tempNickname(board.getTempNickname())
//                        .viewCount(hitCnt)
//                        .commentCount(commentCnt)
//                        .likeCount(likeCnt)
//                        .doLiked(isUserLiked)
////                        .isAuth(user.getIsAuth())   //인증 여부
//                        .build();
//
//                boardListOutputSpecList.add(boardListOutputSpec);
//            }
//        } catch (DataAccessException e) {
//            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
//        }
//
//        return boardListOutputSpecList;
//
//    }

    /**
     * 게시판 작성을 위한 메서드<br/>
     * [로직]<br/>
     * - boardForm에는 board 관련 정보를 담고 있는 객체<br/>
     * - boardForm를 Board객체로 변경<br/>
     *
     * @param boardForm
     * @param userId
     */
    @Override
    @Transactional
    public void registBoard(BoardForm boardForm, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        Board board = Board.builder()
                .title(boardForm.getBoardTitle())
                .content(boardForm.getBoardContent())
                .pictureOne(boardForm.getPictureOne())
                .pictureTwo(boardForm.getPictureTwo())
                .pictureThree(boardForm.getPictureThree())
                .pictureFour(boardForm.getPictureFour())
                .tempNickname(boardForm.getTempNickname())
                .user(user)
                .build();

        // 전체 게시판
        if (boardForm.getGroupId() == null && boardForm.getHospitalId() == null) {
            board.setBoardType(BoardType.ALL);

        } else if (boardForm.getGroupId() != null && boardForm.getHospitalId() == null) {
            //그룹 게시판
//            Group group = groupQueryRepository.findById(boardForm.getGroupId()).orElseThrow(
//                    () -> new GroupException(GroupErrorCode.NOT_FOUND)
//            );
            board.setBoardType(BoardType.GROUP);
//            board.setGroup(group);

        } else if (boardForm.getGroupId() == null && boardForm.getHospitalId() != null) {
            // 병원 게시판
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
     * 게시판 삭제 메서드<br/>
     * [로직]<br/>
     * - 게시판 아이디와 현재 삭제 버튼을 클릭한 유저 아이디를 토대로 게시글을 삭제한다.
     *
     * @param boardId
     * @param userId
     */
    @Override
    public void deleteBoard(Long boardId, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Board board = boardQueryRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        try {
            //작성자가 같다면
            if (board.getUser().getId().equals(userId)) {
                List<Comment> commentListByBoardId = commentQueryRepository.getCommentListByBoardId(boardId);
                commentJpaRepository.deleteAll(commentListByBoardId);
                boardJpaRepository.delete(board); //삭제

            }

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
     *
     * @param boardModifyForm
     * @param userId
     */
    @Override
    @Transactional
    public void modifyBoard(BoardModifyForm boardModifyForm, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Board board = boardQueryRepository.findById(boardModifyForm.getBoardId()).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        try {
            board.setTitle(boardModifyForm.getBoardContent());
            board.setContent(boardModifyForm.getBoardContent());
            board.setPictureOne(boardModifyForm.getPictureOne());
            board.setPictureTwo(boardModifyForm.getPictureTwo());
            board.setPictureThree(boardModifyForm.getPictureThree());
            board.setPictureFour(boardModifyForm.getPictureFour());

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
     */
    @Override
    public BoardOutputSpec getBoard(Long boardId, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Board board = boardQueryRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );
        BoardOutputSpec boardOutputSpec = null;

        try {
            boolean isUserLiked = false;  //좋아요 누른 여부

            //조회수 증가해도 되는지 검증
            if (redisViewCountUtil.checkAndIncrementViewCount(String.valueOf(boardId), String.valueOf(userId))) {
                redisViewCountUtil.incrementViewCount(String.valueOf(boardId));
            }
            long viewCount = redisViewCountUtil.getViewCount(String.valueOf(boardId)); //하루 동안의 조회수

            Long commentCnt = commentQueryRepository.getCommentCount(board.getId());
            Long likeCnt = boardQueryRepository.getLikeCount(board.getId());
            Long hitCnt = viewCount + board.getViewCount();     // DB + redis

            //사용자가 이미 좋아요를 눌렀는지
            if (!isNotLiked(userId, board.getId())) {
                isUserLiked = true;
            }
            List<CommentListOutputSpec> commentList = commentService.getCommentList(boardId);

            boardOutputSpec = BoardOutputSpec.builder()
                    .boardId(boardId)
                    .boardTitle(board.getTitle())
                    .boardContent(board.getContent())
                    .boardCreatedAt(board.getCreatedAt())
                    .pictureOne(board.getPictureOne())
                    .pictureTwo(board.getPictureTwo())
                    .pictureThree(board.getPictureThree())
                    .pictureFour(board.getPictureFour())
                    .viewCount(hitCnt)
                    .groupId(board.getGroup().getId())
                    .userId(user.getId())
                    .tempNickname(board.getTempNickname())
                    .profileImgUrl(user.getProfileImgUrl())
                    .commentCount(commentCnt)
                    .boardLikeCount(likeCnt)
                    .hospitalName(board.getHospital().getHospitalName())
                    .doLiked(isUserLiked)
//                    .isAuth(user.getIsAuth())   //인증 여부
                    .comments(commentList)
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
     */
    @Override
    public void registLike(LikeForm likeForm, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
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
     */
    @Override
    public void deleteLike(LikeForm likeForm, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
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
