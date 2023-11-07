package com.example.ddingjakyo_be.team.domain;

import com.example.ddingjakyo_be.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

  private String kakaoRoomId;

  @Enumerated(EnumType.STRING)
  private String matchStatus;

  @Enumerated(EnumType.STRING)
  private String gender;
}
