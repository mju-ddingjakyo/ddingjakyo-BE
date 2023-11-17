package com.example.ddingjakyo_be.team.controller.dto.response;

import com.example.ddingjakyo_be.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.team.domain.Team;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetOneTeamResponse {

  private Long teamId;

  private String name;

  private int gender;

  private String content;

  private int memberCount;

  private Long leaderId;

  private String matchStatus;

  private List<MemberResponse> membersResponse;

  @Builder
  public GetOneTeamResponse(Long teamId, String name, int gender, String content, int memberCount, Long leaderId, String matchStatus, List<MemberResponse> membersResponse) {
    this.teamId =teamId;
    this.name = name;
    this.gender = gender;
    this.content = content;
    this.memberCount = memberCount;
    this.leaderId = leaderId;
    this.matchStatus = matchStatus;
    this.membersResponse = membersResponse;
  }

  public static GetOneTeamResponse of(Team team, List<MemberResponse> membersResponse) {
    return GetOneTeamResponse.builder()
        .teamId(team.getId())
        .name(team.getName())
        .gender(team.getGender().getIdentifier())
        .content(team.getContent())
        .memberCount(team.getMemberCount())
        .leaderId(team.getLeaderId())
        .matchStatus(String.valueOf(team.getMatchStatus()))
        .membersResponse(membersResponse)
        .build();
  }
}
