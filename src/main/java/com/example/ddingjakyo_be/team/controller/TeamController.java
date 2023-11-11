package com.example.ddingjakyo_be.team.controller;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import com.example.ddingjakyo_be.team.controller.dto.request.CreateTeamRequest;
import com.example.ddingjakyo_be.team.controller.dto.request.UpdateTeamRequest;
import com.example.ddingjakyo_be.team.controller.dto.response.GetAllTeamResponse;
import com.example.ddingjakyo_be.team.controller.dto.response.GetOneTeamResponse;
import com.example.ddingjakyo_be.team.service.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

  private final TeamService teamService;

  @PostMapping("/team")
  public ResponseEntity<ResponseMessage> createTeam(@RequestBody CreateTeamRequest createTeamRequest) {
    teamService.createTeam(createTeamRequest);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/teams")
  public ResponseEntity<ResponseMessage> getAllTeam() {
    List<GetAllTeamResponse> allTeams= teamService.getAllTeam();
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, allTeams);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/team/{teamId}")
  public ResponseEntity<ResponseMessage> getOneTeam(@PathVariable("teamId") Long teamId) {
    //처음에 httpsession 으로 세션이 있는 지 확인, 없으면 로그인 화면으로 이동,
    GetOneTeamResponse team=teamService.getOneTeam(teamId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, team);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @DeleteMapping("/team/{teamId}")
  public ResponseEntity<ResponseMessage> deleteTeam(@PathVariable("teamId") Long teamId){
    //httpsession을 통해 sessionId를 가져오고, sessionId를 deleteTeam에 넘겨준다.
    teamService.deleteTeam(teamId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PutMapping("/team/{teamId}")
  public ResponseEntity<ResponseMessage> updateTeam(@RequestBody UpdateTeamRequest updateTeamRequest, @PathVariable("teamId") Long teamId) {
    teamService.updateTeam(updateTeamRequest, teamId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }
}
