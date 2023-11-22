package com.example.ddingjakyo_be.team.service;

import com.example.ddingjakyo_be.belong.service.BelongService;
import com.example.ddingjakyo_be.common.exception.NoAuthException;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.member.service.MemberService;
import com.example.ddingjakyo_be.team.constant.MatchStatus;
import com.example.ddingjakyo_be.team.controller.dto.request.TeamProfileRequest;
import com.example.ddingjakyo_be.team.controller.dto.response.GetAllTeamResponse;
import com.example.ddingjakyo_be.team.controller.dto.response.GetOneTeamResponse;
import com.example.ddingjakyo_be.team.domain.Team;
import com.example.ddingjakyo_be.team.repository.TeamRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;

  private final MemberService memberService;

  private final BelongService belongService;

  public void createTeam(Long authId, TeamProfileRequest createTeamRequest) {
    validateUserCreatedTeam(authId);

    Set<Member> members = memberService.findMembersByEmails(createTeamRequest.getMembersEmail());
    Member member = memberService.findMemberById(authId);
    members.add(member);

    Team team = createTeamRequest.toEntity(MatchStatus.POSSIBLE, authId);
    validateTeamInfo(members, team);

    teamRepository.save(team);
    belongService.doBelong(members, team);
  }

  @Transactional(readOnly = true)
  public List<GetAllTeamResponse> getAllTeam() {
    List<GetAllTeamResponse> getAllTeamResponses = new ArrayList<>();
    addTeamResponse(getAllTeamResponses);
    return getAllTeamResponses;
  }

  @Transactional(readOnly = true)
  public GetOneTeamResponse getOneTeam(Long teamId) {
    Team team = findTeamById(teamId);
    List<Member> members = findMembersByTeam(team);
    List<MemberResponse> membersResponse = members.stream()
        .map(MemberResponse::from)
        .collect(Collectors.toList());
    return GetOneTeamResponse.of(team, membersResponse);
  }

  public void deleteTeam(Long authId, Long teamId) {
    Team team = findTeamById(teamId);
    isLeader(team, authId);
    teamRepository.delete(team);
  }

  public void updateTeam(Long authId, TeamProfileRequest teamProfileRequest, Long teamId) {
    Team team = findTeamById(teamId);
    isLeader(team, authId);
    Set<Member> members = memberService.findMembersByEmails(teamProfileRequest.getMembersEmail());
    validateTeamInfo(members, team);

    team.update(teamProfileRequest.getName(), teamProfileRequest.getContent(),
        teamProfileRequest.getMemberCount());
    belongService.update(members, team);
  }

  public Team findTeamById(Long teamId) {
    return teamRepository.findById(teamId)
        .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));
  }

  public List<Member> findMembersByTeam(Team team) {
    List<Member> members = new ArrayList<>();
    team.getBelongs().forEach(belong -> members.add(belong.getMember()));
    return members;
  }

  public void isLeader(Team team, Long authId) {
    if (!Objects.equals(team.getLeaderId(), authId)) {
      throw new NoAuthException("리더가 아닙니다.");
    }
  }

  private void addTeamResponse(List<GetAllTeamResponse> getAllTeamResponses) {
    List<Team> teams = teamRepository.findAll();
    for (Team team : teams) {
      List<Member> members = findMembersByTeam(team);
      List<MemberProfileResponse> membersProfile = members.stream()
          .map(MemberProfileResponse::from)
          .collect(Collectors.toList());
      getAllTeamResponses.add(GetAllTeamResponse.of(team, membersProfile));
    }
  }

  private void validateUserCreatedTeam(Long authId) {
    teamRepository.findByLeaderId(authId).ifPresent(team -> {
      throw new NoAuthException("한 명의 유저는 한 팀만 생성할 수 있습니다.");
    });
  }

  private void validateTeamInfo(Set<Member> members, Team team) {
    validateMemberGender(team, members);
    validateMemberCount(team, members);
  }

  private void validateMemberGender(Team team, Set<Member> members) {
    members.stream()
        .filter(member -> !Objects.equals(member.getGender(), team.getGender()))
        .findAny()
        .ifPresent(member -> {
          throw new IllegalArgumentException("멤버의 성별은 팀의 성별과 같아야 합니다.");
        });
  }

  private void validateMemberCount(Team team, Set<Member> members) {
    members.stream()
        .filter(member -> !Objects.equals(members.size(), team.getMemberCount()))
        .findAny()
        .ifPresent(member -> {
          throw new IllegalArgumentException("팀의 멤버수와 입력한 멤버 수가 맞지 않습니다.");
        });
  }
}
