package org.egglog.api.inquiry.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.egglog.api.inquiry.model.dto.response.InquiryDto;
import org.egglog.api.inquiry.model.dto.response.InquiryUserDto;
import org.egglog.api.user.model.entity.User;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "`Inquiries`")
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", length = 2000, nullable = false)
    private String content;

    @Column(name = "group_admin", nullable = false)
    @Enumerated(EnumType.STRING)
    private InquiryType inquiryType;

    @Column(name = "is_noted", nullable = false)
    private Boolean isNoted;

    @CreatedDate
    @Column(name = "inquiry_created_at")
    private LocalDateTime createdAt;  //작성일

    public InquiryDto toDto(){
        InquiryUserDto inquiryUserDto = new InquiryUserDto();
        inquiryUserDto.setUserId(this.user.getId());
        inquiryUserDto.setUserName(this.user.getName());
        inquiryUserDto.setUserEmail(this.user.getEmail());
        inquiryUserDto.setProfileImage(this.user.getProfileImgUrl());
        return InquiryDto.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .inquiryType(this.inquiryType)
                .isNoted(this.isNoted)
                .user(inquiryUserDto)
                .build();
    }
}
