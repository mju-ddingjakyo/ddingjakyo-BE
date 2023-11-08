package com.example.ddingjakyo_be.team.controller.dto.response;

import com.example.ddingjakyo_be.team.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAllTeamResponse {

  private String name;

  private String gender;

  private String content;

  private int memberCount;

  private String matchStatus;

  private List<MemberProfileResponse> memberProfiles;


  @Builder
  public GetAllTeamResponse(String name, String gender, String content, int memberCount, String matchStatus, List<MemberProfileResponse> memberProfiles) {
    this.name = name;
    this.gender = gender;
    this.content = content;
    this.memberCount = memberCount;
    this.matchStatus = matchStatus;
    this.memberProfiles = memberProfiles;
  }

  public static GetAllTeamResponse of(Team team,List<MemberProfileResponse> memberProfiles){
    return GetAllTeamResponse.builder()
        .name(team.getName())
        .gender(team.getGender())
        .content(team.getContent())
        .memberCount(team.getMemberCount())
        .matchStatus(team.getMatchStatus())
        .memberProfiles(memberProfiles)
        .build();
  }
}
