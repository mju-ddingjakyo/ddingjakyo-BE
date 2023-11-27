package com.example.ddingjakyo_be.domain.proposal.controller.dto.response;

import com.example.ddingjakyo_be.domain.proposal.entity.Proposal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApproveMatchingResponse {

  private String kakaoURL;

  @Builder
  public ApproveMatchingResponse(String kakaoURL) {
    this.kakaoURL = kakaoURL;
  }

  public static ApproveMatchingResponse from(Proposal proposal) {
    return ApproveMatchingResponse.builder()
        .kakaoURL(proposal.getKakaoRoomURL())
        .build();
  }
}
