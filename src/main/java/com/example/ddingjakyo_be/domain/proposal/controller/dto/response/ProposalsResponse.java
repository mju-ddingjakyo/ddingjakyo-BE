package com.example.ddingjakyo_be.domain.proposal.controller.dto.response;

import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.domain.team.controller.dto.response.GetAllTeamResponse;
import com.example.ddingjakyo_be.domain.team.entity.Team;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProposalsResponse {

  private GetAllTeamResponse team;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String kakaoURL;

  @Builder
  public ProposalsResponse(GetAllTeamResponse team, String kakaoURL) {
    this.team = team;
    this.kakaoURL = kakaoURL;
  }

  public static ProposalsResponse of(Team team, List<MemberProfileResponse> membersProfile) {
    return ProposalsResponse.builder()
        .team(GetAllTeamResponse.of(team, membersProfile))
        .build();
  }

  public static ProposalsResponse of(Team team, List<MemberProfileResponse> membersProfile, String kakaoURL) {
    return ProposalsResponse.builder()
        .team(GetAllTeamResponse.of(team, membersProfile))
        .kakaoURL(kakaoURL)
        .build();
  }
}
