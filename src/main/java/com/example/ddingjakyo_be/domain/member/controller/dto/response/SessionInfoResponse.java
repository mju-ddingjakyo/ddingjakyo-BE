package com.example.ddingjakyo_be.domain.member.controller.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SessionInfoResponse {

  private String sessionId;

  private Long memberId;

  public SessionInfoResponse(String sessionId, Long memberId) {
    this.sessionId = sessionId;
    this.memberId = memberId;
  }
}
