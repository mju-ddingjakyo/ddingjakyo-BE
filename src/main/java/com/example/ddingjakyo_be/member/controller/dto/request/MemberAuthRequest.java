package com.example.ddingjakyo_be.member.controller.dto.request;

import com.example.ddingjakyo_be.common.constant.Gender;
import com.example.ddingjakyo_be.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAuthRequest {

  private String email;

  private String gender;

  private String password;

  public Member toEntity(String encodedPassword) {
    return Member.builder()
        .email(email)
        .password(encodedPassword)
        .gender(Gender.valueOf(gender))
        .build();
  }
}
