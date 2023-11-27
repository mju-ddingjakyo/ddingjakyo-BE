package com.example.ddingjakyo_be.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {
  OK("요청에 성공하였습니다."),
  BAD_REQUEST("잘못된 요청입니다."),
  NOT_FOUND("찾을 수 없는 페이지 입니다."),
  BAD_CREDENTIAL("아이디 혹은 비밀번호가 일치하지 않습니다."),
  MEMBER_NOT_FOUND("요청하신 멤버를 찾을 수 없습니다."),
  PROFILE_NOT_FOUND("프로필 정보가 없습니다."),
  TEAM_NOT_FOUND("요청한 팀을 찾을 수 없습니다."),
  UNAUTHORIZED("로그인 후 접근 가능합니다."),
  FORBIDDEN("접근 권한이 없습니다."),
  INTERNAL_SEVER_ERROR("서버 에러"),
  IS_EMPTY("현재 조회할 수 있는 것이 없습니다."),
  DUPLICATED_MEMBER("이미 사용 중인 이메일입니다.");

  private final String resultMessage;
}
