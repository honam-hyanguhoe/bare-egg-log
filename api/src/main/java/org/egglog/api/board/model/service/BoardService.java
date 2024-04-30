package org.egglog.api.board.model.service;


import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.board.exception.BoardErrorCode;
import org.egglog.api.board.exception.BoardException;
import org.egglog.api.board.model.dto.params.*;
import org.egglog.api.board.model.dto.response.*;
import org.egglog.api.board.model.entity.*;
import org.egglog.api.board.repository.jpa.board.BoardRepository;
import org.egglog.api.board.repository.jpa.boardLike.BoardLikeRepository;
import org.egglog.api.board.repository.jpa.comment.CommentRepository;
import org.egglog.api.hospital.model.entity.HospitalAuth;
import org.egglog.api.hospital.repository.jpa.HospitalAuthJpaRepository;
import org.egglog.api.search.domain.document.BoardDocument;
import org.egglog.api.search.repository.elasticsearch.SearchRepository;
import org.egglog.api.global.util.RedisViewCountUtil;
import org.egglog.api.hospital.exception.HospitalErrorCode;
import org.egglog.api.hospital.exception.HospitalException;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.hospital.repository.jpa.HospitalQueryRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserQueryRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * ## ${NAME}
 * * packageName    : ${PACKAGE_NAME}
 * * fileName       : ${NAME}
 * author         : ${USER}
 * date           : ${DATE}
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * ${DATE}        ${USER}       최초 생성
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    //사용자
    private final UserQueryRepository userQueryRepository;

    //게시판
    private final BoardRepository boardRepository;

    //좋아요
    private final BoardLikeRepository boardLikeRepository;

    //댓글
    private final CommentRepository commentRepository;

    //병원
    private final HospitalQueryRepository hospitalQueryRepository;
    private final HospitalAuthJpaRepository hospitalAuthJpaRepository;

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
    public List<BoardListOutputSpec> getBoardList(BoardListForm boardListForm, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        List<BoardListOutputSpec> boardListOutputSpecList = new ArrayList<>();
        int size = 10;

        try {
            List<BoardDocument> boardDocuments = searchRepository.searchBoard(boardListForm.getSearchWord(), boardListForm.getGroupId(), boardListForm.getHospitalId(), boardListForm.getLastBoardId(), size);

            for (BoardDocument board : boardDocuments) {
                Long commentCnt = commentRepository.getCommentCount(board.getId());
                Long likeCnt = boardRepository.getLikeCount(board.getId());
                long viewCount = redisViewCountUtil.getViewCount(String.valueOf(board.getId())); //하루 동안의 조회수
                Long hitCnt = viewCount + board.getViewCount();

                //사용자의 병원 인증 정보
                Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(user, user.getSelectedHospital());

                boolean isUserLiked = false;  //좋아요 누른 여부
                boolean isCommented = false;    //댓글 유무 여부

                if (commentCnt != null) {
                    isCommented = true;
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
                        .tempNickname(board.getTempNickname())
                        .viewCount(hitCnt)
                        .commentCount(commentCnt)
                        .likeCount(likeCnt)
                        .isLiked(isUserLiked)  //좋아요 여부
                        .isCommented(isCommented)   //댓글 유무
                        .build();

                if (hospitalAuth.isPresent()) {
                    boardListOutputSpec.setIsHospitalAuth(hospitalAuth.get().getAuth());
                }

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
     * @param user
     * @return
     */
    public List<BoardListOutputSpec> getHotBoardList(User user) {
        List<BoardListOutputSpec> boardListOutputSpecList = new ArrayList<>();

        try {
            String boardIds = redisTemplate.opsForValue().get("hotBoards");
            if (boardIds != null && !boardIds.isEmpty()) {
                List<Long> ids = Arrays.stream(boardIds.replace("[", "").replace("]", "").split(","))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .toList();

                for (Long boardId : ids) {
                    Board board = boardRepository.findById(boardId).orElseThrow(
                            () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
                    );

                    //사용자의 병원 인증 정보
                    Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(user, user.getSelectedHospital());

                    Long commentCnt = commentRepository.getCommentCount(board.getId());
                    Long likeCnt = boardRepository.getLikeCount(board.getId());
                    long viewCount = redisViewCountUtil.getViewCount(String.valueOf(boardId)); //하루 동안의 조회수
                    Long hitCnt = viewCount + board.getViewCount();

                    boolean isUserLiked = false;  //좋아요 누른 여부
                    boolean isCommented = false;    //댓글 유무 여부

                    if (commentCnt != null) {
                        isCommented = true;
                    }

                    //사용자가 이미 좋아요를 눌렀는지
                    if (!isNotLiked(user.getId(), board.getId())) { //아직 좋아요 안눌렀다면 true, 이미 좋아요 눌렀다면 false
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
                            .isLiked(isUserLiked)
                            .isCommented(isCommented)
                            .build();

                    if (hospitalAuth.isPresent()) {
                        boardListOutputSpec.setIsHospitalAuth(hospitalAuth.get().getAuth());
                    }

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


    /**
     * 게시판 작성을 위한 메서드<br/>
     * [로직]<br/>
     * - boardForm에는 board 관련 정보를 담고 있는 객체<br/>
     * - boardForm를 Board객체로 변경<br/>
     *
     * @param boardForm
     * @param userId
     */
    @Transactional
    public void registerBoard(BoardForm boardForm, Long userId) {
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
                .isCommented(false)
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
            boardRepository.save(board);  //저장
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
     * 게시판 삭제 메서드<br/>
     * [로직]<br/>
     * - 게시판 아이디와 현재 삭제 버튼을 클릭한 유저 아이디를 토대로 게시글을 삭제한다.
     *
     * @param boardId
     * @param userId
     */
    public void deleteBoard(Long boardId, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        try {
            //작성자가 같다면
            if (board.getUser().getId().equals(userId)) {
                Optional<List<Comment>> commentListByBoardId = commentRepository.getCommentListByBoardId(boardId);
                commentListByBoardId.ifPresent(commentRepository::deleteAll);
                boardRepository.delete(board); //삭제

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
     * @param boardUpdateForm
     * @param userId
     */
    @Transactional
    public BoardModifyOutputSpec modifyBoard(Long boardId, BoardUpdateForm boardUpdateForm, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );
        BoardModifyOutputSpec boardOutputSpec = null;

        try {
            board.setTitle(boardUpdateForm.getBoardContent());
            board.setContent(boardUpdateForm.getBoardContent());
            board.setPictureOne(boardUpdateForm.getPictureOne());
            board.setPictureTwo(boardUpdateForm.getPictureTwo());
            board.setPictureThree(boardUpdateForm.getPictureThree());
            board.setPictureFour(boardUpdateForm.getPictureFour());

            long viewCount = redisViewCountUtil.getViewCount(String.valueOf(board.getId())); //하루 동안의 조회수
            Long hitCnt = viewCount + board.getViewCount();     // DB + redis
            Long commentCnt = commentRepository.getCommentCount(board.getId());
            Long likeCnt = boardRepository.getLikeCount(board.getId());

            boolean isUserLiked = false;  //좋아요 누른 여부
            boolean isCommented = false;    //댓글 유무 여부

            if (commentCnt != null) {
                isCommented = true;
            }
            //사용자가 이미 좋아요를 눌렀는지
            if (!isNotLiked(userId, board.getId())) {
                isUserLiked = true;
            }
            boardOutputSpec = BoardModifyOutputSpec.builder()
                    .boardId(boardId)
                    .boardTitle(boardUpdateForm.getBoardContent())
                    .boardContent(boardUpdateForm.getBoardContent())
                    .pictureOne(boardUpdateForm.getPictureOne())
                    .pictureTwo(boardUpdateForm.getPictureTwo())
                    .pictureThree(boardUpdateForm.getPictureThree())
                    .pictureFour(boardUpdateForm.getPictureFour())
                    .boardCreatedAt(board.getCreatedAt())
                    .groupId(board.getGroup().getId())
                    .userId(user.getId())
                    .tempNickname(board.getTempNickname())
                    .profileImgUrl(user.getProfileImgUrl())
                    .commentCount(commentCnt)
                    .viewCount(viewCount + hitCnt)
                    .boardLikeCount(likeCnt)
                    .isLiked(isUserLiked)
                    .isCommented(isCommented)
//                    .isHospitalAuth(user.getIsHospitalAuth())
                    .build();

        } catch (DataAccessException e) {
            throw new BoardException(BoardErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoardException(BoardErrorCode.UNKNOWN_ERROR);
        }

        return boardOutputSpec;
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
    public BoardOutputSpec getBoard(Long boardId, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );


        BoardOutputSpec boardOutputSpec = null;

        try {
            //조회수 증가해도 되는지 검증
            if (redisViewCountUtil.checkAndIncrementViewCount(String.valueOf(boardId), String.valueOf(userId))) {
                redisViewCountUtil.incrementViewCount(String.valueOf(boardId));
            }
            long viewCount = redisViewCountUtil.getViewCount(String.valueOf(boardId)); //하루 동안의 조회수

            Long commentCnt = commentRepository.getCommentCount(board.getId());
            Long likeCnt = boardRepository.getLikeCount(board.getId());
            Long hitCnt = viewCount + board.getViewCount();     // DB + redis

            boolean isUserLiked = false;  //좋아요 누른 여부
            boolean isCommented = false;    //댓글 유무 여부

            if (commentCnt != null) {
                isCommented = true;
            }
            //사용자가 이미 좋아요를 눌렀는지
            if (!isNotLiked(userId, board.getId())) {
                isUserLiked = true;
            }
            //사용자의 병원 인증 정보
            Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(user, user.getSelectedHospital());

            //게시물에 대한 댓글(댓글이 없다면 빈 리스트로 리턴)
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
//                    .groupId(board.getGroup().getId())
                    .userId(user.getId())
                    .tempNickname(board.getTempNickname())
                    .profileImgUrl(user.getProfileImgUrl())
                    .commentCount(commentCnt)
                    .boardLikeCount(likeCnt)
//                    .hospitalName(board.getHospital().getHospitalName())
                    .isLiked(isUserLiked)
                    .isCommented(isCommented)
//                    .isHospitalAuth(hospitalAuth.get().getAuth())   //인증 여부
                    .comments(commentList)
                    .build();

            if (hospitalAuth.isPresent()) {
                boardOutputSpec.setIsHospitalAuth(hospitalAuth.get().getAuth());
            }
            if (board.getGroup() != null) {
                boardOutputSpec.setGroupId(board.getGroup().getId());
            }
            if (board.getHospital() != null) {
                boardOutputSpec.setHospitalName(board.getHospital().getHospitalName());
            }

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
    public void registerLike(LikeForm likeForm, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        Board board = boardRepository.findById(likeForm.getBoardId()).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        //아직 좋아요 안눌렀다면
        if (isNotLiked(userId, board.getId())) {
            BoardLike boardLike = BoardLike.builder()
                    .id(likeForm.getBoardId())
                    .board(board)
                    .user(user)
                    .build();

            boardLikeRepository.save(boardLike);
        } else {
            // 이미 좋아요가 있는 경우, 필요하다면 여기서 추가 로직을 처리할 수 있습니다.
            throw new BoardException(BoardErrorCode.ALREADY_LIKE);
        }
    }


    /**
     * 공감(좋아요) 취소 메서드<br/>
     * [로직]<br/>
     * - LikeForm: 좋아요 취소 하려는 boardId를 담고있음.<br/>
     * - boardId를 가지고 공감을 취소 시킨다.
     *
     * @param boardId
     * @param userId
     */
    public void deleteLike(Long boardId, Long userId) {
        User user = userQueryRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        try {
            //이미 좋아요를 눌렀다면
            if (!isNotLiked(userId, boardId)) {
                Optional<BoardLike> boardLike = boardLikeRepository.getBoardLikeByBoardId(boardId);
                boardLikeRepository.delete(boardLike.get());
            } else {
                // 이미 좋아요가 있는 경우, 필요하다면 여기서 추가 로직을 처리할 수 있습니다.
                throw new BoardException(BoardErrorCode.ALREADY_LIKE);
            }
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
        Optional<BoardLike> userBoardLike = boardRepository.getUserBoardLike(boardId, userId);
        return userBoardLike.isEmpty();
    }
}
