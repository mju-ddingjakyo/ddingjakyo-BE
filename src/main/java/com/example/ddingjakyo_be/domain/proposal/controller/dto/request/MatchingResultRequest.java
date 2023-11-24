package com.example.ddingjakyo_be.domain.proposal.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchingResultRequest {

  private Long sendTeamId;

  private boolean matchingResult;
}
