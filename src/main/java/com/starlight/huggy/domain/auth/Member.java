package com.starlight.huggy.domain.auth;

import com.starlight.huggy.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String socialId;

    @Column(unique = true)
    @Size(min = 2, max = 10)
    private String nickname;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = true)
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialLoginType socialType;

    @Builder
    public Member(String socialId, String nickname, String email, String profileImg, SocialLoginType socialLoginType) {
        this.socialId = socialId;
        this.nickname = nickname;
        this.email = email;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.socialType = socialLoginType;
    }
}