package com.example.ddingjakyo_be.team.controller.dto.request;

import com.example.ddingjakyo_be.team.constant.MatchStatus;
import com.example.ddingjakyo_be.team.domain.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTeamRequest {

  private String name;

  private int gender;

  private int memberCount;

  private String content;

  //멤버 이메일들 추가

  public Team toEntity(MatchStatus matchStatus, int leaderId) {
    return Team.builder()
        .name(name)
        .gender("MALE")
        .memberCount(memberCount)
        .leaderId(leaderId)
        .content(content)
        .build();
  }
}
