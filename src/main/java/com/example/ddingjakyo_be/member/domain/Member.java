package com.example.ddingjakyo_be.member.domain;

import com.example.ddingjakyo_be.belong.entity.Belong;
import com.example.ddingjakyo_be.common.entity.BaseEntity;
import com.example.ddingjakyo_be.common.entity.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  private String name;

  private String password;

  private String nickname;

  private Gender gender;

  private String major;

  private int age;

  private String mbti;

  private String introduction;

  private String profile_image;

  @OneToMany(mappedBy = "member")
  private List<Belong> belongs = new ArrayList<>();

}
