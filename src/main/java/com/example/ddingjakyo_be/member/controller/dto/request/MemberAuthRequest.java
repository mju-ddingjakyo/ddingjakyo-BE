package com.example.ddingjakyo_be.member.controller.dto.request;

import com.example.ddingjakyo_be.common.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAuthRequest {

  private String email;

  private Gender gender;

  private String password;
}
