package com.example.ddingjakyo_be.team.controller.dto.request;

import com.example.ddingjakyo_be.common.constant.Gender;
import com.example.ddingjakyo_be.team.constant.MatchStatus;
import com.example.ddingjakyo_be.team.domain.Team;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamProfileRequest {

  @Size(min = 4, max = 10, message = "최소 4글자, 최대 10글자로 입력해주세요.")
  @NotBlank(message = "필수 정보입니다.")
  private String name;

  @Min(value = 0, message = "올바르지 않은 성별 정보입니다.")
  @Max(value = 1, message = "올바르지 않은 성별 정보입니다.")
  @NotNull(message = "필수 정보입니다.")
  private Integer gender;

  @Size(min = 2, max = 5, message = "2명부터 5명까지 가능합니다.")
  @NotNull(message = "필수 정보입니다.")
  private Integer memberCount;

  @Size(min = 10, max = 50, message = "최소 10글자, 최대 50글자로 입력해주세요.")
  @NotBlank(message = "필수 정보입니다.")
  private String content;

  private List<String> membersEmail;
  //멤버 이메일들 추가

  public Team toEntity(MatchStatus matchStatus, Long leaderId) {
    return Team.builder()
        .name(name)
        .gender(Gender.getGender(gender)) //gender를 0, 1로 받아올 지 글자로 받아올진 결정해야함.
        .memberCount(memberCount)
        .leaderId(leaderId)
        .content(content)
        .matchStatus(matchStatus)
        .build();
  }
}
