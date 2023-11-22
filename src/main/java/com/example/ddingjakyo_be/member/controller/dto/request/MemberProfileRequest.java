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

  @NotBlank(message = "필수 정보입니다.")
  @Size(min = 2, max = 6, message = "닉네임은 2글자 이상 6글자 이내로 입력해주세요.")
  private String nickname;

  @NotBlank(message = "필수 정보입니다.")
  private String major;

  @NotBlank(message = "필수 정보입니다.")
  @Size(min = 10, max = 30, message = "소개글은 10자 이상 30자 이내로 입력해주세요.")
  private String introduction;

  @NotNull(message = "필수 정보입니다.")
  @Min(value = 18, message = "만 18세 이상부터 이용가능합니다.")
  private Integer age;

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
