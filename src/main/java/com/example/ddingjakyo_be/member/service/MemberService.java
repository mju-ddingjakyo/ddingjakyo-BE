package com.example.ddingjakyo_be.member.service;

import com.example.ddingjakyo_be.belong.service.BelongService;
import com.example.ddingjakyo_be.member.controller.dto.request.MemberAuthRequest;
import com.example.ddingjakyo_be.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.member.repository.MemberRepository;
import com.example.ddingjakyo_be.team.service.TeamService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public MemberResponse login(final String email, final String password) {
    Member member = findMemberByEmail(email);
    String encodedPassword = (member == null) ? "" : member.getPassword();

    if (member == null || !passwordEncoder.matches(password, encodedPassword)) {
      return null;
    }

    return MemberResponse.from(member);
  }

  public void register(MemberAuthRequest memberAuthRequest) {
    String password = memberAuthRequest.getPassword();
    // 비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(password);

    Member member = memberAuthRequest.toEntity(encodedPassword);
    memberRepository.save(member);
  }

  public MemberResponse getMemberProfileById(final Long memberId) {
    Member member = findMemberById(memberId);
    return MemberResponse.from(member);
  }

  public MemberProfileResponse getMemberProfileByEmail(final String email) {
    Member member = findMemberByEmail(email);
    return MemberProfileResponse.from(member);
  }

  public Member findMemberById(final Long memberId) {
    return memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
  }

  public Member findMemberByEmail(final String email) {
    return memberRepository.findMemberByEmail(email);
  }

  public List<Member> findMembersByEmails(final List<String> emails) {
    List<Member> members = new ArrayList<>();

    for (String email : emails) {
      Member member = findMemberByEmail(email);
      members.add(member);
    }

    return members;
  }

  public void updateMemberProfile(MemberProfileRequest memberProfileRequest, Long memberId) {
    // 세션의 memberId와 비교 후 같으면 수정

    Member member = findMemberById(memberId);

    member.changeNickname(memberProfileRequest.getNickname());
    member.changeMajor(memberProfileRequest.getMajor());
    member.changeAge(memberProfileRequest.getAge());
    member.changeMbti(memberProfileRequest.getMbti());
    member.changeIntroduction(memberProfileRequest.getIntroduction());
    member.changeProfileImage(memberProfileRequest.getProfileImage());

    memberRepository.save(member);
  }

  public void deleteMember(final Long memberId) {
    Member member = findMemberById(memberId);
    // 삭제하려는 클라이언트의 세션 정보의 memberId와 비교 후 같으면 삭제, 아니면 실패 응답
    memberRepository.delete(member);
  }
}
