package com.example.ddingjakyo_be.team.controller.dto.request;

import com.example.ddingjakyo_be.belong.entity.Belong;
import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.team.constant.MatchStatus;
import com.example.ddingjakyo_be.team.domain.Team;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTeamRequest {

  private String name;

  private int gender;

  private int memberCount;

  private String content;

  private List<String> membersEmail;
  //멤버 이메일들 추가

  public Team toEntity(MatchStatus matchStatus, int leaderId) {
    return Team.builder()
        .name(name)
        .gender("MALE") //gender를 0, 1로 받아올 지 글자로 받아올진 결정해야함.
        .memberCount(memberCount)
        .leaderId(leaderId)
        .content(content)
        .build();
  }
}
