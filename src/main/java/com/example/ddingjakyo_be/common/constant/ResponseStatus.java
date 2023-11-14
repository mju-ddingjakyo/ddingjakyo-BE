package com.example.ddingjakyo_be.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {
  OK("요청에 성공하였습니다."),
  BAD_REQUEST("잘못된 요청입니다."),
  NOT_FOUND("찾을 수 없는 페이지 입니다."),
  FORBIDDEN("접근 권한이 없습니다."),
  INTERNAL_SEVER_ERROR("서버 에러");

  private final String resultMessage;
}
