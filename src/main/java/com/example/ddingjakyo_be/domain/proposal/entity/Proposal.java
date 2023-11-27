package com.example.ddingjakyo_be.domain.proposal.entity;

import com.example.ddingjakyo_be.common.entity.BaseEntity;
import com.example.ddingjakyo_be.domain.proposal.constant.ProposalStatus;
import com.example.ddingjakyo_be.domain.team.entity.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Proposal extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private String kakaoRoomURL;

  @Enumerated(EnumType.STRING)
  private ProposalStatus proposalStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SENDER_ID")
  private Team senderTeam;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "RECEIVER_ID")
  private Team receiverTeam;

  @Builder
  public Proposal(String kakaoRoomURL, ProposalStatus proposalStatus, Team senderTeam, Team receiverTeam){
    this.kakaoRoomURL = kakaoRoomURL;
    this.proposalStatus = proposalStatus;
    this.senderTeam = senderTeam;
    this.receiverTeam = receiverTeam;
  }

  public void approveProposal(){
    this.proposalStatus = ProposalStatus.APPROVED;
  }

  public void rejectProposal(){
    this.proposalStatus = ProposalStatus.REJECTED;
  }
}
