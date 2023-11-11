package com.example.ddingjakyo_be.member.controller;

import com.example.ddingjakyo_be.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/member/{memberId}")
  public MemberProfileResponse getMemberById(@PathVariable Long memberId) {
    return memberService.getMemberProfileById(memberId);
  }

  @GetMapping("/member/{email}")
  public MemberProfileResponse getMemberById(@PathVariable String email) {
    return memberService.getMemberProfileByEmail(email);
  }

  @PostMapping("/member")
  public void createMember(@RequestBody MemberProfileRequest memberProfileRequest) {
    memberService.createMember(memberProfileRequest);
  }

  @PutMapping("/member/{memberId}")
  public void updateMemberProfile(MemberProfileRequest updateMemberProfile,
      @PathVariable Long memberId) {
    memberService.updateMemberProfile(updateMemberProfile, memberId);
  }

  @DeleteMapping("/member/{memberId}")
  public void deleteMember(@PathVariable Long memberId) {
    memberService.deleteMember(memberId);
  }
}
