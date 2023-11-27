package com.example.ddingjakyo_be.domain.proposal.controller.dto.response;

import com.example.ddingjakyo_be.domain.proposal.entity.Proposal;
import com.example.ddingjakyo_be.domain.team.controller.dto.response.GetOneTeamResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProposalResponse {
  private GetOneTeamResponse receiveTeam;

  private String matchStatus;
  @Builder
  public ProposalResponse(GetOneTeamResponse receiveTeam, String matchStatus) {
    this.receiveTeam = receiveTeam;
    this.matchStatus = matchStatus;
  }

  public static ProposalResponse from(Proposal proposal, GetOneTeamResponse getOneTeamResponse) {
    return ProposalResponse.builder()
        .receiveTeam(getOneTeamResponse)
        .matchStatus(String.valueOf(proposal.getProposalStatus()))
        .build();
  }
}
