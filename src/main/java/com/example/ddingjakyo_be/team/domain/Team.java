package com.example.ddingjakyo_be.team.domain;

import com.example.ddingjakyo_be.belong.entity.Belong;
import com.example.ddingjakyo_be.common.entity.BaseEntity;
import com.example.ddingjakyo_be.common.entity.Gender;
import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.team.constant.MatchStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Team extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private int memberCount;

  private int leaderId;

  private String content;

  @Enumerated(EnumType.STRING)
  private MatchStatus matchStatus;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @OneToMany(mappedBy = "senderTeam")
  private final List<Proposal> sendProposals = new ArrayList<>();

  @OneToMany(mappedBy = "receiverTeam")
  private final List<Proposal> receiveProposals = new ArrayList<>();

  @OneToMany(mappedBy = "team")
  private final List<Belong> belongs = new ArrayList<>();

  @Builder
  public Team(String name, int memberCount, int leaderId, String content, String matchStatus,
      String gender) {
    this.name = name;
    this.memberCount = memberCount;
    this.leaderId = leaderId;
    this.content = content;
    this.matchStatus = MatchStatus.valueOf(matchStatus);
    this.gender = Gender.valueOf(gender);
  }
}
