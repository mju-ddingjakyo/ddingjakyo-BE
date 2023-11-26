package com.example.ddingjakyo_be.domain.member.controller;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import com.example.ddingjakyo_be.domain.member.controller.dto.request.MemberAuthRequest;
import com.example.ddingjakyo_be.domain.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.EmailConfirmResponse;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.domain.member.service.EmailService;
import com.example.ddingjakyo_be.domain.member.service.MemberService;
import com.example.ddingjakyo_be.domain.member.controller.dto.request.EmailConfirmRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
      final @RequestBody @Valid MemberAuthRequest memberAuthRequest) {
    ResponseMessage responseMessage;
    memberService.register(memberAuthRequest);
    responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PostMapping("/email_certification")
  public ResponseEntity<ResponseMessage> certify(
      @RequestParam(value = "email", required = false) final String email) throws Exception {
    EmailConfirmResponse response = emailService.sendEmail(email);
    return createEmailConfirmResponse(response);
  }

  @PostMapping("/email_certification/confirm")
  public ResponseEntity<ResponseMessage> confirm(
      final @RequestBody EmailConfirmRequest emailConfirmRequest) {
    EmailConfirmResponse response = emailService.checkVerificationCode(emailConfirmRequest);
    return createEmailConfirmResponse(response);
  }

  @GetMapping("/member/my")
  public ResponseEntity<ResponseMessage> getMyPage(
      @SessionAttribute("memberId") final Long myAuthId) {
    MemberResponse memberResponse = memberService.getMemberProfileById(myAuthId);
    return createProfileResponse(memberResponse);
  }

  @GetMapping("/member/{memberId}")
  public ResponseEntity<ResponseMessage> getMemberById(@PathVariable final Long memberId) {
    MemberResponse memberResponse = memberService.getMemberProfileById(memberId);
    return createProfileResponse(memberResponse);
  }

  @GetMapping("/member")
  public ResponseEntity<ResponseMessage> getMemberByEmail(
      @RequestParam(value = "email", required = false) final String email) {
    MemberProfileResponse memberProfile = memberService.getMemberProfileByEmail(email);
    ResponseMessage responseMessage;
    responseMessage = ResponseMessage.of(ResponseStatus.OK, memberProfile);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PostMapping("/member")
  public ResponseEntity<ResponseMessage> createMemberProfile(
      @SessionAttribute("memberId") final Long memberId,
      @ModelAttribute @Valid final MemberProfileRequest memberProfileRequest) {
    ResponseMessage responseMessage;
    memberService.createMemberProfile(memberId, memberProfileRequest);
    responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PutMapping("/member")
  public ResponseEntity<ResponseMessage> updateMemberProfile(
      @SessionAttribute(value = "memberId", required = false) final Long myAuthId,
      @ModelAttribute @Valid final MemberProfileRequest updateMemberProfile) {
    memberService.updateMemberProfile(updateMemberProfile, myAuthId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @DeleteMapping("/member")
  public ResponseEntity<ResponseMessage> deleteMember(
      @SessionAttribute(value = "memberId", required = false) final Long myAuthId,
      HttpServletRequest request) {
    memberService.deleteMember(myAuthId);
    request.getSession().invalidate();   // 세션 무효화
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  private ResponseEntity<ResponseMessage> createEmailConfirmResponse(
      final EmailConfirmResponse response) {
    ResponseMessage responseMessage;
    HttpStatus httpStatus;

    if (!response.isSuccess()) {
      responseMessage = ResponseMessage.of(ResponseStatus.BAD_REQUEST, response);
      httpStatus = HttpStatus.BAD_REQUEST;
    } else {
      responseMessage = ResponseMessage.of(ResponseStatus.OK, response);
      httpStatus = HttpStatus.OK;
    }

    return new ResponseEntity<>(responseMessage, httpStatus);
  }

  private ResponseEntity<ResponseMessage> createProfileResponse(
      final MemberResponse memberResponse) {
    ResponseMessage responseMessage;
    HttpStatus httpStatus;

    // 멤버 프로필 필수 정보인 닉네임, 전공, 소개가 없으면 프로필을 생성하지 않았다는 메시지 반환
    if (memberResponse.getNickname() == null || memberResponse.getMajor() == null
        || memberResponse.getIntroduction() == null) {
      responseMessage = ResponseMessage.of(ResponseStatus.PROFILE_NOT_FOUND);
      httpStatus = HttpStatus.BAD_REQUEST;
    } else {
      responseMessage = ResponseMessage.of(ResponseStatus.OK);
      httpStatus = HttpStatus.OK;
    }

    return new ResponseEntity<>(responseMessage, httpStatus);
  }
}
