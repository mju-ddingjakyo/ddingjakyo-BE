package com.example.ddingjakyo_be.member.domain;

import com.example.ddingjakyo_be.belong.entity.Belong;
import com.example.ddingjakyo_be.common.constant.Gender;
import com.example.ddingjakyo_be.common.entity.BaseEntity;
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
import lombok.Builder;
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

  private String nickname;

  private String password;

  private Gender gender;

  private String major;

  private int age;

  private String mbti;

  private String introduction;

  private String profileImage;

  @OneToMany(mappedBy = "member")
  private List<Belong> belongs = new ArrayList<>();

  @Builder
  public Member(String email, String nickname, String password, Gender gender,
      String major,
      int age, String mbti, String introduction, String profileImage) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.gender = gender;
    this.major = major;
    this.age = age;
    this.mbti = mbti;
    this.introduction = introduction;
    this.profileImage = profileImage;
  }

  public void changeNickname(String nickname) {
    this.nickname = nickname;
  }

  public void changeMajor(String major) {
    this.major = major;
  }

  public void changeAge(int age) {
    this.age = age;
  }

  public void changeMbti(String mbti) {
    this.mbti = mbti;
  }

  public void changeIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public void changeProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }
}
