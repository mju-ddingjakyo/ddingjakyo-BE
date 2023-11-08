package com.example.ddingjakyo_be.team.service;

import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.team.constant.MatchStatus;
import com.example.ddingjakyo_be.team.controller.dto.request.CreateTeamRequest;
import com.example.ddingjakyo_be.team.controller.dto.response.GetAllTeamResponse;
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
  public void createTeam(CreateTeamRequest createTeamRequest) {
    List<Member> members = memberService.findAllByEmail(createTeamRequest.getMembersEmail());
    //한명의 유저는 하나의 팀만 생성 가능하다
    //leaderID로 만든 팀이 이미 있다면 예외 처리
    //leaderID는 httpsession의 getsession을 통해 userid값을 받아온다.
    Team team = createTeamRequest.toEntity(MatchStatus.POSSIBLE,1);
    teamRepository.save(team);
    belongService.doBelong(members, team);
  }

  public List<GetAllTeamResponse> getAllTeam() {
    List<GetAllTeamResponse> getAllTeamResponses = new ArrayList<>();
    addTeamResponse(getAllTeamResponses);
    return getAllTeamResponses;
  }

  private void addTeamResponse(List<GetAllTeamResponse> getAllTeamResponses){
    List<Team> teams = teamRepository.findAll();
    for (Team team : teams) {
      List<Member> members = belongService.findAllByTeam(team);
      List<MemberProfileResponse> memberProfileResponses = members.stream()
          .map(MemberProfileResponse::from)
          .collect(Collectors.toList());
      getAllTeamResponses.add(GetAllTeamResponse.of(team,memberProfileResponses));
    }
  }
}
