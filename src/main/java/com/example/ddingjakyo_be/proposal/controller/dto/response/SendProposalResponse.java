package com.example.ddingjakyo_be.proposal.controller.dto.response;

import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.team.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendProposalResponse {
  private Team recieveTeam;

  private String matchStatus;
  @Builder
  public SendProposalResponse(Team recieveTeam, String matchStatus) {
    this.recieveTeam = recieveTeam;
    this.matchStatus = matchStatus;
  }

  public static SendProposalResponse from(Proposal proposal) {
    return SendProposalResponse.builder()
        .recieveTeam(proposal.getReceiverTeam())
        .matchStatus(String.valueOf(proposal.getProposalStatus()))
        .build();
  }
}
