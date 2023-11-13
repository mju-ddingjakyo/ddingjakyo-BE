package com.example.ddingjakyo_be.proposal.controller.dto.response;

import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.team.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReceiveProposalResponse {

  private Team sendTeam;

  @Builder
  public ReceiveProposalResponse(Team sendTeam) {
    this.sendTeam = sendTeam;
  }

  public static ReceiveProposalResponse from(Proposal proposal) {
    return ReceiveProposalResponse.builder()
        .sendTeam(proposal.getSenderTeam())
        .build();
  }
}
