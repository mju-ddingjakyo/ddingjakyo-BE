package com.example.ddingjakyo_be.member.controller.dto.request;

import com.example.ddingjakyo_be.common.constant.Gender;
import com.example.ddingjakyo_be.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthRequest {

  @Email
  private String email;

  @NotNull
  private int gender;

  // 공백 없이 8자리 ~ 20자리 이내 최소 하나 이상의 숫자, 영문자, 특수 문자를 포함해야 하는 정규식
  @NotBlank
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+=-])(?=\\S+$).{8,20}$")
  private String password;

  public Member toEntity(String encodedPassword, int userInput) {
    return Member.builder()
        .email(email)
        .password(encodedPassword)
        .gender(Gender.getGender(userInput))
        .build();
  }
}
