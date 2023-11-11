package com.example.ddingjakyo_be.member.controller.dto.response;

import com.example.ddingjakyo_be.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberProfileResponse {

  private String nickname;

  private String major;

  private String introduction;

  private int age;

  private String mbti;

  private String profileImage;

  @Builder
  public MemberProfileResponse(String nickname, String major, String introduction, int age,
      String mbti, String profileImage) {
    this.nickname = nickname;
    this.major = major;
    this.introduction = introduction;
    this.age = age;
    this.mbti = mbti;
    this.profileImage = profileImage;
  }

  public static MemberProfileResponse from(Member member) {
    return MemberProfileResponse.builder()
        .nickname(member.getNickname())
        .major(member.getMajor())
        .introduction(member.getIntroduction())
        .age(member.getAge())
        .mbti(member.getMbti())
        .profileImage(member.getProfileImage())
        .build();
  }
}
