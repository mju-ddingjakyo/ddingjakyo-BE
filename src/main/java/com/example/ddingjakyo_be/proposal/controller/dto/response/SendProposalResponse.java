package com.example.ddingjakyo_be.proposal.controller.dto.response;

import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.team.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendProposalResponse {
  private Team sendProposalTeam;

  @Builder
  public SendProposalResponse(Team sendProposalTeam) {
    this.sendProposalTeam = sendProposalTeam;
  }

  public static SendProposalResponse from(Proposal proposal) {
    return SendProposalResponse.builder()
        .sendProposalTeam(proposal.getSenderTeam())
        .build();
  }
}
