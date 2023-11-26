package com.example.ddingjakyo_be.domain.member.service;

import com.example.ddingjakyo_be.aws.S3Service;
import com.example.ddingjakyo_be.domain.belong.domain.Belong;
import com.example.ddingjakyo_be.domain.belong.service.BelongService;
import com.example.ddingjakyo_be.domain.member.controller.dto.request.MemberAuthRequest;
import com.example.ddingjakyo_be.domain.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.domain.member.domain.Member;
import com.example.ddingjakyo_be.domain.member.repository.MemberRepository;
import com.example.ddingjakyo_be.domain.team.domain.Team;
import com.example.ddingjakyo_be.domain.team.service.TeamService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final S3Service s3Service;
//  private final BelongService belongService;

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

  public void updateMemberProfile(MemberProfileRequest memberProfileRequest, Long memberId) {
    Member member = findMemberById(memberId);
    deletePrevImage(member.getProfileImage());
    Member updateMember = memberProfileRequest.toEntity(
        getUploadImageFileName(memberProfileRequest));
    member.updateMemberProfile(updateMember);
  }

  private void deletePrevImage(String profileImage) {
    if (profileImage != null) {
      s3Service.delete(profileImage);
    }
  }

  public void deleteMember(final Long memberId) {
    Member member = findMemberById(memberId);
    // 삭제하려는 클라이언트의 세션 정보의 memberId와 비교 후 같으면 삭제, 아니면 실패 응답
    memberRepository.delete(member);
  }

  public Set<Member> findMembersByEmails(final List<String> emails) {
//    for (String email : emails) {
//      Member member = memberRepository.findMemberByEmail(email)
//          .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
//      Long id = member.getId();
//      // 멤버로 현재 소속된 팀 찾기
////      Team team = belongService.findTeamByMemberId(id);
//
//      // 초대할 멤버가 속한 팀의 상태가 "매칭 대기"인가?
//      //
//
//    }

    return emails.stream()
        .map(email -> memberRepository.findMemberByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("이메일이 틀렸습니다."))
        )
        .collect(Collectors.toSet());
  }

  public Member findMemberById(Long memberId) {
    return memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
  }

  private String getUploadImageFileName(MemberProfileRequest memberProfileRequest) {
    String fileName = "";
    for (MultipartFile multipartFile : memberProfileRequest.getImage()) {
      // 파일명 지정 (겹치면 안되고, 확장자 빼먹지 않도록 조심!)
      fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
      // 파일데이터와 파일명 넘겨서 S3에 저장
      try {
        s3Service.upload(multipartFile, fileName);
      } catch (IOException e) {
        log.warn("이미지 업로드 실패");
        throw new IllegalArgumentException("이미지 업로드 실패");
      }
    }
    return fileName;
  }
}
