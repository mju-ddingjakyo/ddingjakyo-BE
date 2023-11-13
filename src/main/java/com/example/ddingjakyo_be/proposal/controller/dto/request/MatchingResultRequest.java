package com.example.ddingjakyo_be.proposal.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchingResultRequest {

  private Long senderId;

  private boolean matchingResult;
}
