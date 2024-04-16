package org.egglog.api.board.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.group.model.entity.Group;
import org.egglog.api.user.model.entity.Users;

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

    private String pictureOne;

    private String pictureTwo;

    private String pictureThree;

    private String pictureFour;

    @Enumerated(EnumType.STRING)
    private BoardType boardType; //ALL, HOSPITAL, DEPARTMENT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
}
