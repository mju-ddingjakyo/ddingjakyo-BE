package com.example.ddingjakyo_be.domain.proposal.controller.dto.response;

import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.domain.team.controller.dto.response.GetAllTeamResponse;
import com.example.ddingjakyo_be.domain.team.entity.Team;
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

  public static ProposalsResponse from(Team team, List<MemberProfileResponse> membersProfile) {
    return ProposalsResponse.builder()
        .sendTeam(GetAllTeamResponse.of(team, membersProfile))
        .build();
  }
}
