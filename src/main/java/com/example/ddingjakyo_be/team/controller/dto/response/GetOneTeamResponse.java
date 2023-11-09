package com.example.ddingjakyo_be.team.controller.dto.response;

import com.example.ddingjakyo_be.team.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetOneTeamResponse {

  private String name;

  private String gender;

  private String content;

  private int memberCount;

  private int leaderId;

  private String matchStatus;

  private List<TeamMemberResponse> teamMembers;

  @Builder
  public GetOneTeamResponse(String name, String gender, String content, int memberCount, int leaderId, String matchStatus, List<TeamMemberResponse> teamMembers) {
    this.name = name;
    this.gender = gender;
    this.content = content;
    this.memberCount = memberCount;
    this.leaderId = leaderId;
    this.matchStatus = matchStatus;
    this.teamMembers = teamMembers;
  }

  public static GetOneTeamResponse of(Team team, List<TeamMemberResponse> teamMembers) {
    return GetOneTeamResponse.builder()
        .name(team.getName())
        .gender(team.getGender())
        .content(team.getContent())
        .memberCount(team.getMemberCount())
        .leaderId(team.getLeaderId())
        .matchStatus(team.getMatchStatus())
        .teamMembers(teamMembers)
        .build()
  }
}
