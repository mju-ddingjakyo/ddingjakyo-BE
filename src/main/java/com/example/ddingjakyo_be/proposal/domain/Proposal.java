package com.example.ddingjakyo_be.proposal.domain;

import com.example.ddingjakyo_be.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
  private String proposalStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SENDER_ID")
  private Team SenderTeam;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "RECIVER_ID")
  private Team ReciverTeam;
}