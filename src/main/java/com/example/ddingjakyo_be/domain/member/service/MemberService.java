package com.example.ddingjakyo_be.domain.member.service;

import com.example.ddingjakyo_be.aws.S3Service;
import com.example.ddingjakyo_be.common.exception.custom.MemberNotFoundException;
import com.example.ddingjakyo_be.domain.member.controller.dto.request.MemberAuthRequest;
import com.example.ddingjakyo_be.domain.member.controller.dto.request.MemberProfileRequest;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.EmailConfirmResponse;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.domain.member.entity.Member;
import com.example.ddingjakyo_be.domain.member.repository.MemberRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final S3Service s3Service;

  public MemberResponse login(final String email, final String password) {
    Member member = memberRepository.findMemberByEmail(email)
        .orElseThrow(IllegalArgumentException::new);
    String encodedPassword = (member == null) ? "" : member.getPassword();

    if (member == null || !passwordEncoder.matches(password, encodedPassword)) {
      return null;
    }

    return MemberResponse.from(member);
  }

  public boolean register(MemberAuthRequest memberAuthRequest) {
    // 중복 이메일 검사
    String email = memberAuthRequest.getEmail();
    EmailConfirmResponse uniqueEmail = checkDuplicatedEmail(email);
    if (!uniqueEmail.isSuccess()) { // 중복 이메일이 존재하면
      return true;
    }

    String password = memberAuthRequest.getPassword();
    // 비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(password);
    int gender = memberAuthRequest.getGender();

    Member member = memberAuthRequest.toEntity(encodedPassword, gender);
    memberRepository.save(member);
    return false;
  }

  public void createMemberProfile(Long memberId, MemberProfileRequest memberProfileRequest) {
    updateMemberProfile(memberProfileRequest, memberId);
  }

  @Transactional(readOnly = true)
  public MemberResponse getMemberProfileById(final Long memberId) {
    Member member = findMemberById(memberId);
    return MemberResponse.from(member);
  }

  @Transactional(readOnly = true)
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

  public EmailConfirmResponse checkDuplicatedEmail(final String email) {
    String message;
    boolean success;

    Optional<Member> member = memberRepository.findMemberByEmail(email);

    if (member.isEmpty()) {
      success = true;
      message = "사용 가능한 이메일입니다.";
    } else {
      success = false;
      message = "이미 사용 중인 이메일입니다.";
    }

    return EmailConfirmResponse.of(success, message);
  }

  @Transactional(readOnly = true)
  public Set<Member> findMembersByEmails(final List<String> emails) {

    return emails.stream()
        .map(email -> memberRepository.findMemberByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("이메일이 틀렸습니다."))
        )
        .collect(Collectors.toSet());
  }

  @Transactional(readOnly = true)
  public Member findMemberById(Long memberId) {
    return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
  }

  private String getUploadImageFileName(MemberProfileRequest memberProfileRequest) {
    String fileName = "";

    if (memberProfileRequest.getProfileImage() == null) {
      return null;
    }

    for (MultipartFile multipartFile : memberProfileRequest.getProfileImage()) {

      if (multipartFile == null) {
        break;
      }
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
