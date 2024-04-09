package org.egglog.api.user.model.entity;

import com.nursetest.app.user.model.entity.enums.AuthProvider;
import com.nursetest.app.user.model.entity.enums.UserRole;
import com.nursetest.app.user.model.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * @Entity
 * @Table(name = "users")
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    /**
     * @Id
     * @GeneratedValue(strategy = GenerationType.IDENTITY)
     * @Column(name = "user_id")
     */
    private Long id;

    /**
     * @Column(nullable = false, unique = true, length = 254)
     */
    private String email;

    /**
     * @Column(length = 255)
     */
    private String password;

    /**
     * @Column(nullable = false, length = 50)
     */
    private String userName;

    /**
     * @Enumerated(EnumType.STRING)
     * @Column(nullable = false)
     */
    private AuthProvider provider;

    /**
     * @Column(nullable = false, length = 100)
     */
    private String hospitalName;

    /**
     * @Column(nullable = false, length = 30)
     */
    private String empNo;

    /**
     * @Column(length = 2000)
     */
    private String profileImgUrl;

    /**
     * @Enumerated(EnumType.STRING)
     * @Column(nullable = false)
     */
    private UserRole role;

    /**
     * @Enumerated(EnumType.STRING)
     * @Column(nullable = false)
     */
    private UserStatus status;

    /**
     * @Column(nullable = false)
     */
    private LocalDateTime createdAt;

    /**
     * @Column(nullable = false)
     */
    private LocalDateTime updatedAt;


}

