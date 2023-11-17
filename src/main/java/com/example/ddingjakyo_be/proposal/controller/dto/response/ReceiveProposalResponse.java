package com.example.ddingjakyo_be.proposal.controller.dto.response;

import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.team.controller.dto.response.GetAllTeamResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReceiveProposalResponse {

  private GetAllTeamResponse sendTeam;

  @Builder
  public ReceiveProposalResponse(GetAllTeamResponse sendTeam) {
    this.sendTeam = sendTeam;
  }

  public static ReceiveProposalResponse from(Proposal proposal, List<MemberProfileResponse> membersProfile) {
    return ReceiveProposalResponse.builder()
        .sendTeam(GetAllTeamResponse.of(proposal.getSenderTeam(), membersProfile))
        .build();
  }
}
