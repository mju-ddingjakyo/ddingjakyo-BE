package com.example.ddingjakyo_be.team.service;

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

  public void createTeam(CreateTeamRequest createTeamRequest) {
    List<Member> members = memberService.findMembersByEmail(createTeamRequest.getMembersEmail());
    //한명의 유저는 하나의 팀만 생성 가능하다
    //leaderID로 만든 팀이 이미 있다면 예외 처리
    //leaderID는 httpsession의 getsession을 통해 userid값을 받아온다.
    Team team = createTeamRequest.toEntity(MatchStatus.POSSIBLE, 1);
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

  public void deleteTeam(Long teamId) {
    Team team = findTeamById(teamId);
    //받아온 session id로 유저 정보를 찾고,
    //유저의 아이디와 team의 leader id가 같은지 확인
    //같다면 삭제하고, 같지 않다면 예외처리
    teamRepository.delete(team);
  }

  public void updateTeam(UpdateTeamRequest updateTeamRequest, Long teamId) {
    Team team = findTeamById(teamId);
    //받아온 session id로 유저 정보를 찾고,
    //유저의 아이디와 team의 leader id가 같은지 확인
    //같다면 삭제하고, 같지 않다면 예외처리
    team.update(updateTeamRequest.getName(), updateTeamRequest.getContent(), updateTeamRequest.getMemberCount());

    List<Member> members = memberService.findMembersByEmail(updateTeamRequest.getMembersEmail());
    belongService.update(team, members);
  }

  public Team findTeamById(Long teamId){
    return teamRepository.findById(teamId).orElseThrow(IllegalArgumentException::new);
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

  private List<Member> findMembersByTeam(Team team){
    List<Member> members = new ArrayList<>();
    team.getBelongs().forEach(belong -> members.add(belong.getMember()));
    return members;
  }
}
