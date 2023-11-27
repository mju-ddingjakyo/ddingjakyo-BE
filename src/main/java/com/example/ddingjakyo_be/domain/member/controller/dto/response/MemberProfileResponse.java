package com.example.ddingjakyo_be.domain.member.controller.dto.response;

import com.example.ddingjakyo_be.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberProfileResponse {

  private String nickname;

  private String email;

  private String profileImage;

  @Builder
  public MemberProfileResponse(String nickname, String email, String profileImage) {
    this.nickname = nickname;
    this.email = email;
    this.profileImage = profileImage;
  }

  public static MemberProfileResponse from(Member member) {
    return MemberProfileResponse.builder()
        .nickname(member.getNickname())
        .email(member.getEmail())
        .profileImage(member.getProfileImage())
        .build();
  }
}
