package com.example.ddingjakyo_be.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
      return false;
    }

    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}
