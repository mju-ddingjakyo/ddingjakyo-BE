package com.example.ddingjakyo_be.belong.domain;

import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.team.domain.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Belong {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "TEAM_ID")
  private Team team;

  public static Belong belongTo(Member member, Team team) {
    Belong belong = new Belong();
    belong.member = member;
    belong.team = team;
    return belong;
  }


}
