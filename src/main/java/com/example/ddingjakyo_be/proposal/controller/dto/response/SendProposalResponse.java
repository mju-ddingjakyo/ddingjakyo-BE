package com.example.ddingjakyo_be.proposal.controller.dto.response;

import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.team.controller.dto.response.GetOneTeamResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendProposalResponse {
  private GetOneTeamResponse recieveTeam;

  private String matchStatus;
  @Builder
  public SendProposalResponse(GetOneTeamResponse recieveTeam, String matchStatus) {
    this.recieveTeam = recieveTeam;
    this.matchStatus = matchStatus;
  }

  public static SendProposalResponse from(Proposal proposal, GetOneTeamResponse getOneTeamResponse) {
    return SendProposalResponse.builder()
        .recieveTeam(getOneTeamResponse)
        .matchStatus(String.valueOf(proposal.getProposalStatus()))
        .build();
  }
}
