package com.example.ddingjakyo_be.interceptor;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

public class LogInCheckInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    // 1. 세션에서 회원 정보 조회
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute("memberId");

    // 2. 회원 정보 체크
    if (memberId == null) {
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");

      // ResponseEntity 객체 생성
      ResponseEntity<ResponseMessage> responseEntity = ResponseEntity.status(
          HttpStatus.UNAUTHORIZED).body(ResponseMessage.of(ResponseStatus.UNAUTHORIZED));

      // ResponseEntity 객체를 JSON으로 변환
      ObjectMapper objectMapper = new ObjectMapper();
      String responseJson = objectMapper.writeValueAsString(responseEntity.getBody());

      response.getWriter().write(responseJson);
      return false;
    }

    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}

