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
public class ProposalsResponse {

  private GetAllTeamResponse sendTeam;

  @Builder
  public ProposalsResponse(GetAllTeamResponse sendTeam) {
    this.sendTeam = sendTeam;
  }

  public static ProposalsResponse from(Proposal proposal, List<MemberProfileResponse> membersProfile) {
    return ProposalsResponse.builder()
        .sendTeam(GetAllTeamResponse.of(proposal.getSenderTeam(), membersProfile))
        .build();
  }
}
