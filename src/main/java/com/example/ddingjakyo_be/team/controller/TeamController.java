package com.example.ddingjakyo_be.team.controller;

import com.example.ddingjakyo_be.team.controller.dto.request.CreateTeamRequest;
import com.example.ddingjakyo_be.team.controller.dto.response.GetAllTeamResponse;
import com.example.ddingjakyo_be.team.service.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

  private final TeamService teamService;

  @PostMapping("/team")
  public void createTeam(@RequestBody CreateTeamRequest createTeamRequest){
    teamService.createTeam(createTeamRequest);
  }

  @GetMapping("/teams")
  public List<GetAllTeamResponse> getAllTeam(){
    return teamService.getAllTeam();
  }


}
