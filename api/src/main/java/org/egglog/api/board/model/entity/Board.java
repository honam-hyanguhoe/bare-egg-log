package org.egglog.api.board.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.group.model.entity.Group;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.user.model.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Column(name = "board_content", nullable = false)
    private String content;

    @Column(name = "board_created_at")
    private LocalDateTime createdAt;

    @Column(name = "picture_one")
    private String pictureOne;

    @Column(name = "picture_two")
    private String pictureTwo;

    @Column(name = "picture_three")
    private String pictureThree;

    @Column(name = "picture_four")
    private String pictureFour;

    @Column(name = "temp_nickname")
    private String tempNickname; //익명 닉네임

    @Column(name = "view_count")
    private long viewCount;  //조회수

    @Column(name = "is_commented")
    private Boolean isCommented;    //댓글 유무

    @Enumerated(EnumType.STRING)
    private BoardType boardType; //ALL, HOSPITAL, GROUP

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
