package com.example.ddingjakyo_be.team.controller.dto.response;

import com.example.ddingjakyo_be.team.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAllTeamResponse {

  private String name;

  private int gender;

  private String content;

  private int memberCount;

  private String matchStatus;

  private List<TeamMemberProfileResponse> teamMembersProfile;


  @Builder
  public GetAllTeamResponse(String name, int gender, String content, int memberCount, String matchStatus, List<TeamMemberProfileResponse> teamMembersProfile) {
    this.name = name;
    this.gender = gender;
    this.content = content;
    this.memberCount = memberCount;
    this.matchStatus = matchStatus;
    this.teamMembersProfile = teamMembersProfile;
  }

  public static GetAllTeamResponse of(Team team, List<TeamMemberProfileResponse> teamMembersProfile) {
    return GetAllTeamResponse.builder()
        .name(team.getName())
        .gender(team.getGender().getIdentifier())
        .content(team.getContent())
        .memberCount(team.getMemberCount())
        .matchStatus(String.valueOf(team.getMatchStatus()))
        .teamMembersProfile(teamMembersProfile)
        .build();
  }
}
