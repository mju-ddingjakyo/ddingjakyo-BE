package com.example.ddingjakyo_be.member.controller;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import com.example.ddingjakyo_be.member.controller.dto.request.EmailConfirmRequest;
import com.example.ddingjakyo_be.member.controller.dto.request.MemberAuthRequest;
import com.example.ddingjakyo_be.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.member.controller.dto.response.EmailConfirmResponse;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.member.service.EmailService;
import com.example.ddingjakyo_be.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

  private final MemberService memberService;
  private final EmailService emailService;

  @PostMapping("/login")
  public ResponseEntity<ResponseMessage> login(HttpServletRequest request) {
    // 회원 정보 조회
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    MemberResponse member = memberService.login(email, password);

    // 세션에 정보 저장, 유지 시간 설정
    if (member != null) {
      HttpSession session = request.getSession();
      session.setAttribute("memberId", member.getId());
      session.setMaxInactiveInterval(30 * 60); // 세션 유지 시간은 30분으로 설정

      ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
      return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.BAD_CREDENTIAL),
        HttpStatus.BAD_GATEWAY);
  }

  @PostMapping("/logout")
  public ResponseEntity<ResponseMessage> logout(HttpSession session) {
    session.invalidate();
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<ResponseMessage> register(
      @RequestBody MemberAuthRequest memberAuthRequest) {
    memberService.register(memberAuthRequest);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PostMapping("/email_certification")
  public ResponseEntity<ResponseMessage> certify(
      @RequestParam(value = "email", required = false) String email) throws MessagingException {
    EmailConfirmResponse response = emailService.sendEmail(email);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, response);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PostMapping("/email_certification/confirm")
  public ResponseEntity<ResponseMessage> confirm(
      @RequestBody EmailConfirmRequest emailConfirmRequest) {
    EmailConfirmResponse response = emailService.checkVerificationCode(emailConfirmRequest);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, response);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/member/{memberId}")
  public ResponseEntity<ResponseMessage> getMemberById(@PathVariable Long memberId) {
    MemberResponse memberResponse = memberService.getMemberProfileById(memberId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, memberResponse);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/member")
  public ResponseEntity<ResponseMessage> getMemberById(
      @RequestParam(value = "email", required = false) String email) {
    MemberProfileResponse memberProfile = memberService.getMemberProfileByEmail(email);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, memberProfile);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PostMapping("/member")
  public ResponseEntity<ResponseMessage> createMemberProfile(
      @SessionAttribute("memberId") Long memberId,
      @RequestBody MemberProfileRequest memberProfileRequest) {
    memberService.createMemberProfile(memberId, memberProfileRequest);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PutMapping("/member/{memberId}")
  public ResponseEntity<ResponseMessage> updateMemberProfile(
      @SessionAttribute(value = "memberId", required = false) Long authId,
      @RequestBody MemberProfileRequest updateMemberProfile,
      @PathVariable Long memberId) {

    if (Objects.equals(authId, memberId)) {
      memberService.updateMemberProfile(updateMemberProfile, memberId);
      ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
      return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
  }

  @DeleteMapping("/member/{memberId}")
  public ResponseEntity<ResponseMessage> deleteMember(
      @SessionAttribute(value = "memberId", required = false) Long authId,
      @PathVariable Long memberId, HttpServletRequest request) {

    if (Objects.equals(authId, memberId)) {
      memberService.deleteMember(memberId);
      // 세션 무효화
      request.getSession().invalidate();
      ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
      return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
  }
}
