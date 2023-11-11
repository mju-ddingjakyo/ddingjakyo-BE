package com.example.ddingjakyo_be.member.service;

import com.example.ddingjakyo_be.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.member.repository.MemberRepository;
import com.example.ddingjakyo_be.team.service.TeamService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  private final TeamService teamService;

  private final BelongService belongService;

  public void createMember(MemberProfileRequest memberProfileRequest) {
    Member member = memberProfileRequest.toEntity();
    memberRepository.save(member);
  }

  public MemberProfileResponse getMemberProfileById(Long memberId) {
    Member member = findMemberById(memberId);
    return MemberProfileResponse.from(member);
  }

  public MemberProfileResponse getMemberProfileByEmail(String email) {
    Member member = findMemberByEmail(email);
    return MemberProfileResponse.from(member);
  }

  private Member findMemberById(Long memberId) {
    return memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
  }

  private Member findMemberByEmail(String email) {
    return memberRepository.findMemberByEmail(email);
  }

  public void updateMemberProfile(MemberProfileRequest updateMemberProfile, Long memberId) {
    Member member = findMemberById(memberId);
    member.changeNickname(updateMemberProfile.getNickname());
    member.changeMajor(updateMemberProfile.getMajor());
    member.changeAge(updateMemberProfile.getAge());
    member.changeMbti(updateMemberProfile.getMbti());
    member.changeIntroduction(updateMemberProfile.getIntroduction());
    member.changeProfileImage(updateMemberProfile.getProfileImage());
  }

  public void deleteMember(Long memberId) {
    Member member = findMemberById(memberId);
    // 삭제하려는 클라이언트의 세션 정보의 memberId와 비교 후 같으면 삭제, 아니면 실패 응답
    memberRepository.delete(member);
  }
}
