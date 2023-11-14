package com.example.ddingjakyo_be.team.service;

import com.example.ddingjakyo_be.belong.service.BelongService;
import com.example.ddingjakyo_be.common.exception.NoAuthException;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberResponse;
import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.member.service.MemberService;
import com.example.ddingjakyo_be.team.constant.MatchStatus;
import com.example.ddingjakyo_be.team.controller.dto.request.CreateTeamRequest;
import com.example.ddingjakyo_be.team.controller.dto.request.UpdateTeamRequest;
import com.example.ddingjakyo_be.team.controller.dto.response.GetAllTeamResponse;
import com.example.ddingjakyo_be.team.controller.dto.response.GetOneTeamResponse;
import com.example.ddingjakyo_be.team.domain.Team;
import com.example.ddingjakyo_be.team.repository.TeamRepository;
import java.util.ArrayList;
import java.util.List;
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

  public void createTeam(Long authId, CreateTeamRequest createTeamRequest) {
    List<Member> members = memberService.findMembersByEmails(createTeamRequest.getMembersEmail());
    //leaderid는 하나의 팀만 만들 수 있다.
    checkUserCreatedTeam(authId);
    Team findTeam = belongService.findTeamByMemberId(authId);
    Team team = createTeamRequest.toEntity(MatchStatus.POSSIBLE, authId);
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

  public void updateTeam(Long authId, UpdateTeamRequest updateTeamRequest, Long teamId) {
    Team team = findTeamById(teamId);
    isLeader(team, authId);
    team.update(updateTeamRequest.getName(), updateTeamRequest.getContent(), updateTeamRequest.getMemberCount());

    List<Member> members = memberService.findMembersByEmails(updateTeamRequest.getMembersEmail());
    belongService.update(members, team);
  }

  public Team findTeamById(Long teamId) {
    return teamRepository.findById(teamId).orElseThrow(IllegalArgumentException::new);
  }

  private void checkUserCreatedTeam(Long authId){
    teamRepository.findByLeaderId(authId).ifPresent(team->{throw new IllegalArgumentException();});
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

  private void isLeader(Team team, Long authId) {
    if(team.getLeaderId() != authId){
      throw new NoAuthException();
    }
  }

  private List<Member> findMembersByTeam(Team team) {
    List<Member> members = new ArrayList<>();
    team.getBelongs().forEach(belong -> members.add(belong.getMember()));
    return members;
  }
}
