package com.example.ddingjakyo_be.domain.member.service;

import com.example.ddingjakyo_be.domain.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.domain.member.domain.Member;
import com.example.ddingjakyo_be.domain.member.repository.MemberRepository;
import com.example.ddingjakyo_be.domain.member.controller.dto.request.MemberAuthRequest;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    Member member = memberRepository.findMemberByEmail(email)
        .orElseThrow(IllegalArgumentException::new);
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
    int gender = memberAuthRequest.getGender();

    Member member = memberAuthRequest.toEntity(encodedPassword, gender);
    memberRepository.save(member);
  }

  public void createMemberProfile(Long memberId, MemberProfileRequest memberProfileRequest) {
    updateMemberProfile(memberProfileRequest, memberId);
  }

  public MemberResponse getMemberProfileById(final Long memberId) {
    Member member = findMemberById(memberId);
    return MemberResponse.from(member);
  }

  public MemberProfileResponse getMemberProfileByEmail(final String email) {
    Member member = memberRepository.findMemberByEmail(email)
        .orElseThrow(IllegalArgumentException::new);
    return MemberProfileResponse.from(member);
  }

  public Set<Member> findMembersByEmails(final List<String> emails) {
      return emails.stream()
          .map(email -> memberRepository.findMemberByEmail(email).orElseThrow(()-> new IllegalArgumentException("이메일이 틀렸습니다")))
          .collect(Collectors.toSet());
  }

  public Member findMemberById(Long memberId) {
    return memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
  }

  public void updateMemberProfile(MemberProfileRequest memberProfileRequest, Long memberId) {
    // 세션의 memberId와 비교 후 같으면 수정
    Member member = findMemberById(memberId);
    System.out.println("ID: " + memberId);
    System.out.println(memberProfileRequest.getNickname());

    Member updateMember = memberProfileRequest.toEntity();
    member.updateMemberProfile(updateMember);
  }

  public void deleteMember(final Long memberId) {
    Member member = findMemberById(memberId);
    // 삭제하려는 클라이언트의 세션 정보의 memberId와 비교 후 같으면 삭제, 아니면 실패 응답
    memberRepository.delete(member);
  }
}
