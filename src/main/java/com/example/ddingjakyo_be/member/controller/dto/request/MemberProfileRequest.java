package com.example.ddingjakyo_be.member.controller.dto.request;

import com.example.ddingjakyo_be.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberProfileRequest {

  private String nickname;

  private String major;

  private String introduction;

  private int age;

  private String mbti;

  private String profileImage;

  public Member toEntity() {
    return Member.builder()
        .nickname(nickname)
        .major(major)
        .introduction(introduction)
        .age(age)
        .mbti(mbti)
        .profileImage(profileImage)
        .build();
  }
}
