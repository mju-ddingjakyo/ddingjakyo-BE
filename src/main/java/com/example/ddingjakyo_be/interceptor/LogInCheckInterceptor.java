package com.example.ddingjakyo_be.interceptor;

import com.example.ddingjakyo_be.member.controller.dto.response.MemberResponse;
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
    MemberResponse member = (MemberResponse) session.getAttribute("member");

    // 2. 회원 정보 체크
    if (member == null) {
      return false;
    }

    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}
