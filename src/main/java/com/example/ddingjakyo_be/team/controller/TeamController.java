package com.example.ddingjakyo_be.team.controller;

import com.example.ddingjakyo_be.team.controller.dto.request.CreateTeamRequest;
import com.example.ddingjakyo_be.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

  private final TeamService teamService;

  @PostMapping
  public void createTeam(@RequestBody CreateTeamRequest createTeamRequest){
    teamService.createTeam(createTeamRequest);
  }

  @GetMapping("{teamId)")
  public void getOneTeam(){

  }


}
