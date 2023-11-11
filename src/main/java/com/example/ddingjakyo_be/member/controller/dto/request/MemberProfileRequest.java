package com.example.ddingjakyo_be.member.controller.dto.request;

import com.example.ddingjakyo_be.member.domain.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

@Getter
@NoArgsConstructor
public class MemberProfileRequest {

  private String nickname;

  private String major;

  private String introduction;

  private int age;

  private String mbti;

  private String profileImage;

  private LocalDateTime created_at;

  public Member toEntity() {
    return Member.builder()
        .nickname(nickname)
        .major(major)
        .introduction(introduction)
        .age(age)
        .mbti(mbti)
        .profileImage(profileImage)
        .build();
  }
}
