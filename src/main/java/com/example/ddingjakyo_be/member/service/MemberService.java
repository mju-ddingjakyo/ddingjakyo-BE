package com.example.ddingjakyo_be.member.service;

import com.example.ddingjakyo_be.member.controller.dto.request.MemberAuthRequest;
import com.example.ddingjakyo_be.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public MemberResponse login(final String email, final String password) throws NotFoundException {
    Member member = memberRepository.findMemberByEmail(email)
        .orElseThrow(NotFoundException::new);
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

  public void createMemberProfile(Long memberId, MemberProfileRequest memberProfileRequest)
      throws NotFoundException {
    updateMemberProfile(memberProfileRequest, memberId);
  }

  public MemberResponse getMemberProfileById(final Long memberId) throws NotFoundException {
    Member member = findMemberById(memberId);
    return MemberResponse.from(member);
  }

  public MemberProfileResponse getMemberProfileByEmail(final String email)
      throws NotFoundException {
    Member member = memberRepository.findMemberByEmail(email)
        .orElseThrow(NotFoundException::new);
    return MemberProfileResponse.from(member);
  }

  public List<Member> findMembersByEmails(final List<String> emails) {
    List<Member> members = new ArrayList<>();

    for (String email : emails) {
      Member member = memberRepository.findMemberByEmail(email)
          .orElseThrow(IllegalArgumentException::new);
      members.add(member);
    }

    return members;
  }

  public Member findMemberById(Long memberId) throws NotFoundException {
    return memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
  }

  public void updateMemberProfile(MemberProfileRequest memberProfileRequest, Long memberId)
      throws NotFoundException {
    Member member = findMemberById(memberId);
    Member updateMember = memberProfileRequest.toEntity();
    member.updateMemberProfile(updateMember);
  }

  public void deleteMember(final Long memberId) throws NotFoundException {
    Member member = findMemberById(memberId);
    memberRepository.delete(member);
  }
}
