package com.example.ddingjakyo_be.member.controller.dto.request;

import com.example.ddingjakyo_be.member.domain.Member;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberProfileRequest {

  @NotBlank
  @Size(min = 2, max = 6)
  private String nickname;

  @NotNull
  private String major;

  @NotNull
  @Size(min = 10, max = 30)
  private String introduction;

  @Min(18)
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
