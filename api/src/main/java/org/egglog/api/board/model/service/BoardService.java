package org.egglog.api.board.model.service;


import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
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
import org.egglog.api.board.repository.redis.RealTimeBoardRepository;
import org.egglog.api.group.exception.GroupErrorCode;
import org.egglog.api.group.exception.GroupException;
import org.egglog.api.group.model.dto.response.GroupPreviewDto;
import org.egglog.api.group.model.entity.Group;
import org.egglog.api.group.repository.jpa.GroupRepository;
import org.egglog.api.hospital.model.entity.HospitalAuth;
import org.egglog.api.hospital.repository.jpa.HospitalAuthJpaRepository;
import org.egglog.api.hospital.repository.jpa.HospitalJpaRepository;
import org.egglog.api.global.util.RedisViewCountUtil;
import org.egglog.api.hospital.exception.HospitalErrorCode;
import org.egglog.api.hospital.exception.HospitalException;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.notification.model.entity.FCMTopic;
import org.egglog.api.notification.model.entity.enums.TopicEnum;
import org.egglog.api.notification.model.service.FCMService;
import org.egglog.api.notification.model.service.NotificationService;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * ## ${NAME}
 * * packageName    : ${PACKAGE_NAME}
 * * fileName       : ${NAME}
 * author         : 김도휘
 * date           : ${DATE}
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * ${DATE}          김도휘             최초 생성
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    //사용자
    private final UserJpaRepository userJpaRepository;

    //게시판
    private final BoardRepository boardRepository;

    //좋아요
    private final BoardLikeRepository boardLikeRepository;

    //댓글
    private final CommentRepository commentRepository;

    //병원
    private final HospitalAuthJpaRepository hospitalAuthJpaRepository;
    private final HospitalJpaRepository hospitalJpaRepository;

    //그룹
    private final GroupRepository groupRepository;

    private final RedisViewCountUtil redisViewCountUtil;    //조회수

    private final StringRedisTemplate redisTemplate;    //급상승 게시물

    private final NotificationService notificationService; //알림

    private final RealTimeBoardRepository realTimeBoardRepository; // 급상승 게시물

    /**
     * 게시판 조회
     * 검색 포함
     *
     * @param boardListForm
     * @param user
     * @return
     */
    public List<BoardListOutputSpec> getBoardList(BoardListForm boardListForm, User user) {
        List<BoardListOutputSpec> boardListOutputSpecList = new ArrayList<>();
        int size = 10;
        try {
            log.info("boardList 쿼리 실행");
            List<BoardListOutputSpec> boardList = boardRepository.findBoardList(boardListForm.getSearchWord(), boardListForm.getGroupId(), boardListForm.getHospitalId(), boardListForm.getOffset(), size, user.getId());

            for (BoardListOutputSpec board : boardList) {
                log.info("boardId: {}", board.getBoardId());

                long viewCount = redisViewCountUtil.getViewCount(String.valueOf(board.getBoardId())); //하루 동안의 조회수
                log.info("redis view count: {}", viewCount);
                long hitCnt = viewCount + board.getViewCount();
                log.info("redis + db 조회수 합산: {}", hitCnt);
                log.info("db 조회수: {}", board.getViewCount());

//                boolean isUserLiked = false;  //좋아요 누른 여부
                boolean isCommented = false;    //댓글 유무 여부

                if (board.getCommentCount() != 0) {
                    isCommented = true;
                }

                //로그인한 사용자가 이미 좋아요를 눌렀는지
//                if (!isNotLiked(user.getId(), board.getBoardId())) { //아직 좋아요 안눌렀다면 true, 이미 좋아요 눌렀다면 false
//                    isUserLiked = true;
//                }

                board.setViewCount(hitCnt);
                board.setIsLiked(board.getIsLiked());
                board.setIsCommented(isCommented);

                boardListOutputSpecList.add(board);
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
     * 좋아요 + 댓글 상위 2개
     *
     * @param user
     * @return
     */
    public List<BoardListOutputSpec> getHotBoardList(User user) {
        List<BoardListOutputSpec> boardListOutputSpecList = new ArrayList<>();
        List<RealTimeBoard> hotBoardList = realTimeBoardRepository.findAll();
        try {
            for (RealTimeBoard hotBoard : hotBoardList) {
                Board board = boardRepository.findWithUserById(hotBoard.getBoardId()).orElseThrow(
                        () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
                );

                //작성자
                User writer = userJpaRepository.findById(board.getUser().getId()).orElseThrow(
                        () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
                );

                //사용자의 병원 인증 정보
                Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(writer, writer.getSelectedHospital());
                long commentCnt = commentRepository.getCommentCount(board.getId());
                long likeCnt = boardRepository.getLikeCount(board.getId());
                long viewCount = redisViewCountUtil.getViewCount(String.valueOf(hotBoard.getBoardId())); //하루 동안의 조회수
                long hitCnt = viewCount + board.getViewCount();

                boolean isUserLiked = false;  //좋아요 누른 여부
                boolean isCommented = false;    //댓글 유무 여부

                if (commentCnt == 0) {
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
                        .userId(writer.getId())
                        .hospitalName(writer.getSelectedHospital().getHospitalName())
                        .build();

                //병원 인증배지가 없다면
                if (hospitalAuth.isEmpty()) {
                    boardListOutputSpec.setIsHospitalAuth(false);
                }
                //있다면
                hospitalAuth.ifPresent(auth -> boardListOutputSpec.setIsHospitalAuth(auth.getAuth()));

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
     * 게시판 작성을 위한 메서드<br/>
     * [로직]<br/>
     * - boardForm에는 board 관련 정보를 담고 있는 객체<br/>
     * - boardForm를 Board객체로 변경<br/>
     *
     * @param boardForm
     * @param user
     */
    @Transactional
    public void registerBoard(BoardForm boardForm, User user) {
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

        Group group = null;
        // 전체 게시판
        if (boardForm.getGroupId() == null && boardForm.getHospitalId() == null) {
            board.setBoardType(BoardType.ALL);

        } else if (boardForm.getGroupId() != null && boardForm.getHospitalId() == null) {
            //그룹 게시판
            group = groupRepository.findById(boardForm.getGroupId()).orElseThrow(
                    () -> new GroupException(GroupErrorCode.NOT_FOUND)
            );
            board.setBoardType(BoardType.GROUP);
            board.setGroup(group);

        } else if (boardForm.getGroupId() == null && boardForm.getHospitalId() != null) {
            // 병원 게시판
            //글 작성자가 해당 병원 게시판에 인증이 되었는지 검증
            Optional<HospitalAuth> byUserAndHospital = hospitalAuthJpaRepository.findByUserAndHospital(user, user.getSelectedHospital());
            if (byUserAndHospital.isEmpty()) {
                throw new BoardException(BoardErrorCode.HOSPITAL_AUTH_ERROR); //해당 병원 인증이 필요합니다.
            }

            Hospital hospital = hospitalJpaRepository.findById(boardForm.getHospitalId()).orElseThrow(
                    () -> new HospitalException(HospitalErrorCode.NOT_FOUND)
            );
            board.setBoardType(BoardType.HOSPITAL);
            board.setHospital(hospital);

        } else {
            throw new BoardException(BoardErrorCode.NO_EXIST_CATEGORY);  //존재하지 않는 게시판 카테코리입니다.
        }

        try {
            Board saveBoard = boardRepository.save(board);//저장
            notificationService.registerBoardNotification(boardForm, saveBoard, group);

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
     * @param user
     */
    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );
        if (!board.getUser().getId().equals(user.getId())) {
            throw new BoardException(BoardErrorCode.NOT_SAME_WRITER);
        }

        try {
            Optional<List<Comment>> commentListByBoardId = commentRepository.getCommentListByBoardId(boardId);
            if (commentListByBoardId.isPresent()) {
                commentRepository.deleteAll(commentListByBoardId.get());
            }
            boardRepository.delete(board); //삭제

            //금일 조회수 삭제
            redisViewCountUtil.deleteViewCountBoard(String.valueOf(boardId));
            redisViewCountUtil.deleteViewCount(String.valueOf(boardId), String.valueOf(user.getId()));


            //급상승 게시물에서 삭제
            Optional<RealTimeBoard> hotBoard = realTimeBoardRepository.findByBoardId(boardId);
            if (hotBoard.isPresent()) {
                realTimeBoardRepository.delete(hotBoard.get());
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
     * @param user
     */
    @Transactional
    public BoardModifyOutputSpec modifyBoard(Long boardId, BoardUpdateForm boardUpdateForm, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );
        if (!board.getUser().getId().equals(user.getId())) {
            throw new BoardException(BoardErrorCode.NOT_SAME_WRITER);
        }
        BoardModifyOutputSpec boardOutputSpec = null;

        try {
            board.setTitle(boardUpdateForm.getBoardTitle());
            board.setContent(boardUpdateForm.getBoardContent());
            board.setPictureOne(boardUpdateForm.getPictureOne());
            board.setPictureTwo(boardUpdateForm.getPictureTwo());
            board.setPictureThree(boardUpdateForm.getPictureThree());
            board.setPictureFour(boardUpdateForm.getPictureFour());

            //사용자의 병원 인증 정보
            Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(user, user.getSelectedHospital());

            long viewCount = redisViewCountUtil.getViewCount(String.valueOf(board.getId())); //하루 동안의 조회수
            long hitCnt = viewCount + board.getViewCount();     // DB + redis
            long commentCnt = commentRepository.getCommentCount(board.getId());
            long likeCnt = boardRepository.getLikeCount(board.getId());

            boolean isUserLiked = false;  //좋아요 누른 여부
            boolean isCommented = false;    //댓글 유무 여부

            if (commentCnt == 0) {
                isCommented = true;
            }

            //사용자가 이미 좋아요를 눌렀는지
            if (!isNotLiked(user.getId(), board.getId())) {
                isUserLiked = true;
            }
            boardOutputSpec = BoardModifyOutputSpec.builder()
                    .boardId(boardId)
                    .boardTitle(boardUpdateForm.getBoardTitle())
                    .boardContent(boardUpdateForm.getBoardContent())
                    .pictureOne(boardUpdateForm.getPictureOne())
                    .pictureTwo(boardUpdateForm.getPictureTwo())
                    .pictureThree(boardUpdateForm.getPictureThree())
                    .pictureFour(boardUpdateForm.getPictureFour())
                    .boardCreatedAt(board.getCreatedAt())
                    .userId(user.getId())
                    .tempNickname(board.getTempNickname())
                    .profileImgUrl(user.getProfileImgUrl())
                    .commentCount(commentCnt)
                    .viewCount(viewCount + hitCnt)
                    .boardLikeCount(likeCnt)
                    .isLiked(isUserLiked)
                    .isCommented(isCommented)
                    .build();

            //병원 인증배지가 없다면
            if (hospitalAuth.isEmpty()) {
                boardOutputSpec.setIsHospitalAuth(false);
            }
            if (hospitalAuth.isPresent()) {
                boardOutputSpec.setIsHospitalAuth(hospitalAuth.get().getAuth());
            }
            if (board.getGroup() != null) {
                boardOutputSpec.setGroupId(board.getGroup().getId());
            }
            if (board.getHospital() != null) {
                boardOutputSpec.setHospitalId(board.getHospital().getId());
            }
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
     * @param user
     * @return
     */
    public BoardOutputSpec getBoard(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );
        //작성자
        User writer = userJpaRepository.findById(board.getUser().getId()).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        BoardOutputSpec boardOutputSpec = null;

        try {
            //조회수 증가해도 되는지 검증
            if (redisViewCountUtil.checkAndIncrementViewCount(String.valueOf(boardId), String.valueOf(user.getId()))) {
                redisViewCountUtil.incrementViewCount(String.valueOf(boardId));
            }
            long viewCount = redisViewCountUtil.getViewCount(String.valueOf(boardId)); //하루 동안의 조회수

            long commentCnt = commentRepository.getCommentCount(board.getId());
            long likeCnt = boardRepository.getLikeCount(board.getId());
            long hitCnt = viewCount + board.getViewCount();     // DB + redis

            boolean isUserLiked = false;  //좋아요 누른 여부
            boolean isCommented = false;  //댓글 유무 여부

            if (commentCnt != 0) {
                isCommented = true;
            }
            //로그인 사용자가 이미 좋아요를 눌렀는지
            if (!isNotLiked(user.getId(), board.getId())) {
                isUserLiked = true;
            }
            //사용자의 병원 인증 정보
            Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(writer, writer.getSelectedHospital());

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
                    .userId(writer.getId())
                    .tempNickname(board.getTempNickname())
                    .profileImgUrl(writer.getProfileImgUrl())
                    .commentCount(commentCnt)
                    .boardLikeCount(likeCnt)
                    .isLiked(isUserLiked)
                    .isCommented(isCommented)
                    .hospitalName(writer.getSelectedHospital().getHospitalName())
                    .build();

            //병원 인증배지가 없다면
            if (hospitalAuth.isEmpty()) {
                boardOutputSpec.setIsHospitalAuth(false);
            } else {
                boardOutputSpec.setIsHospitalAuth(hospitalAuth.get().getAuth());
            }

            if (board.getGroup() != null) {
                boardOutputSpec.setGroupId(board.getGroup().getId());
            }
            if (board.getHospital() != null) {
                boardOutputSpec.setHospitalId(board.getHospital().getId());

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
     * @param user
     */
    public void registerLike(LikeForm likeForm, User user) {
        Board board = boardRepository.findById(likeForm.getBoardId()).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        //아직 좋아요 안눌렀다면
        if (isNotLiked(user.getId(), board.getId())) {
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
     * @param user
     */
    public void deleteLike(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardException(BoardErrorCode.NO_EXIST_BOARD)
        );

        try {
            //이미 좋아요를 눌렀다면
            if (!isNotLiked(user.getId(), boardId)) {
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

    /**
     * 사용자의 병원 아이디, 병원 이름, 그룹 아이디, 그룹 이름
     *
     * @param user
     * @return
     */
    public BoardTypeListOutputSpec getBoardTypeListByUser(User user) {
        //사용자가 선택한 병원
        Hospital selectedHospital = user.getSelectedHospital();
        BoardTypeListOutputSpec boardTypeListOutputSpec = BoardTypeListOutputSpec.builder()
                .hospitalId(selectedHospital.getId())
                .hospitalName(selectedHospital.getHospitalName())
                .build();

        //사용자의 그룹리스트
        List<GroupPreviewDto> groupByUserId = groupRepository.findGroupByUserId(user.getId());
        boardTypeListOutputSpec.setGroupList(groupByUserId);
        return boardTypeListOutputSpec;
    }
}
