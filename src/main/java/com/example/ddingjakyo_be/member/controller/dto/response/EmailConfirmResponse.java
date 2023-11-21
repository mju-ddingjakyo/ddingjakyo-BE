package com.example.ddingjakyo_be.member.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfirmResponse {

  private boolean success;
  private String message;

  public static EmailConfirmResponse of(boolean success, String message) {
    return new EmailConfirmResponse(success, message);
  }
}
