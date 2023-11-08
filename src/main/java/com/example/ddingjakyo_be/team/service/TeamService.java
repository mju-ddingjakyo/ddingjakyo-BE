package com.example.ddingjakyo_be.team.service;

import com.example.ddingjakyo_be.team.constant.MatchStatus;
import com.example.ddingjakyo_be.team.controller.dto.request.CreateTeamRequest;
import com.example.ddingjakyo_be.team.domain.Team;
import com.example.ddingjakyo_be.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  public void createTeam(CreateTeamRequest createTeamRequest) {
    Team team = createTeamRequest.toEntity(MatchStatus.POSSIBLE,1);
    teamRepository.save(team);
  }
}
