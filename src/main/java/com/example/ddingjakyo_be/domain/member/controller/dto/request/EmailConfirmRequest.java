package com.example.ddingjakyo_be.domain.member.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailConfirmRequest {

  private String email;

  private String authCode;
}
