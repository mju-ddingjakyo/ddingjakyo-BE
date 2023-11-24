package com.example.ddingjakyo_be.domain.team.controller.dto.response;

import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.domain.team.domain.Team;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAllTeamResponse {

  private Long teamId;

  private String name;

  private int gender;

  private String content;

  private int memberCount;

  private String matchStatus;

  private List<MemberProfileResponse> membersProfile;


  @Builder
  public GetAllTeamResponse(Long teamId, String name, int gender, String content, int memberCount, String matchStatus, List<MemberProfileResponse> membersProfile) {
    this.teamId =teamId;
    this.name = name;
    this.gender = gender;
    this.content = content;
    this.memberCount = memberCount;
    this.matchStatus = matchStatus;
    this.membersProfile = membersProfile;
  }

  public static GetAllTeamResponse of(Team team, List<MemberProfileResponse> membersProfile) {
    return GetAllTeamResponse.builder()
        .teamId(team.getId())
        .name(team.getName())
        .gender(team.getGender().getIdentifier())
        .content(team.getContent())
        .memberCount(team.getMemberCount())
        .matchStatus(String.valueOf(team.getMatchStatus()))
        .membersProfile(membersProfile)
        .build();
  }
}
