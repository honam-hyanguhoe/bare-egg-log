//package org.egglog.api.board.model.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.egglog.api.user.model.entity.Users;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder(toBuilder = true)
//public class Comment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "comment_id")
//    private Long id;
//
//    @Column(name = "comment_content")
//    private String content;  //내용
//
//    @Column(name = "parent_id")
//    private Long parentId;  //부모 댓글
//
//    @Column(name = "comment_depth")
//    private int depth;  //댓글 깊이
//
//    private String tempNickname;    //익명 닉네임
//
//    @Column(name = "comment_created_at")
//    private LocalDateTime createdAt;  //작성일
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private Users user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "board_id")
//    private Board board;
//}
