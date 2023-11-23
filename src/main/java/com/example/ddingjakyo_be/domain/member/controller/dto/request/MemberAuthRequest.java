package com.example.ddingjakyo_be.domain.member.controller.dto.request;

import com.example.ddingjakyo_be.common.constant.Gender;
import com.example.ddingjakyo_be.domain.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAuthRequest {

  @Email(message = "올바른 이메일 형식이 아닙니다.")
  @NotBlank(message = "필수 정보입니다.")
  private String email;

  @Min(value = 0, message = "올바르지 않은 성별 정보입니다.")
  @Max(value = 1, message = "올바르지 않은 성별 정보입니다.")
  @NotNull(message = "필수 정보입니다.")
  private Integer gender;

  @NotBlank(message = "필수 정보입니다.")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+=-])(?=\\S+$).{8,20}$",
      message = "공백 없이 8자리 ~ 20자리 이내 최소 하나 이상의 숫자, 영문자, 특수 문자를 포함해야 합니다.")
  private String password;

  public Member toEntity(String encodedPassword, int userInput) {
    return Member.builder().email(email).password(encodedPassword)
        .gender(Gender.getGender(userInput)).build();
  }
}
