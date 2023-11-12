package com.example.ddingjakyo_be.member.controller;

import com.example.ddingjakyo_be.member.controller.dto.request.MemberAuthRequest;
import com.example.ddingjakyo_be.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/login")
  @ResponseBody
  public MemberResponse login(HttpServletRequest request) {

    // 회원 정보 조회
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    MemberResponse member = memberService.login(email, password);

    // 세션에 정보 저장, 유지 시간 설정
    if (member != null) {
      HttpSession session = request.getSession();
      session.setAttribute("member", member);
    }

    return member;
  }

  @PostMapping("/logout")
  public void logout(HttpSession session) {
    session.invalidate();
  }

  @GetMapping("/member/{memberId}")
  public MemberResponse getMemberById(@PathVariable Long memberId) {
    return memberService.getMemberProfileById(memberId);
  }

  @GetMapping("/member/{email}")
  public MemberProfileResponse getMemberById(@PathVariable String email) {
    return memberService.getMemberProfileByEmail(email);
  }

  @PutMapping("/member/{memberId}")
  public void updateMemberProfile(@SessionAttribute("member") MemberResponse member,
      MemberProfileRequest updateMemberProfile,
      @PathVariable Long memberId) {
    Long authId = member.getId();

    if (Objects.equals(authId, memberId)) {
      memberService.updateMemberProfile(updateMemberProfile, memberId);
    }

  }

  @DeleteMapping("/member/{memberId}")
  public void deleteMember(@SessionAttribute("member") MemberResponse member,
      @PathVariable Long memberId, HttpServletRequest request) {
    Long authId = member.getId();

    if (Objects.equals(authId, memberId)) {
      memberService.deleteMember(memberId);
      // 세션 무효화
      request.getSession().invalidate();
    }
  }
}
