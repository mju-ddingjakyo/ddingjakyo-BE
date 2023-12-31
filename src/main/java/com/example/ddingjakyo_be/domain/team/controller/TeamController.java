package com.example.ddingjakyo_be.domain.team.controller;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import com.example.ddingjakyo_be.domain.team.controller.dto.request.TeamProfileRequest;
import com.example.ddingjakyo_be.domain.team.controller.dto.response.GetAllTeamResponse;
import com.example.ddingjakyo_be.domain.team.controller.dto.response.GetOneTeamResponse;
import com.example.ddingjakyo_be.domain.team.service.TeamService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

  private final TeamService teamService;

  @PostMapping("/team")
  public ResponseEntity<ResponseMessage> createTeam(@SessionAttribute("memberId") final Long authId,
      @RequestBody @Valid TeamProfileRequest teamProfileRequest) {
    teamService.createTeam(authId, teamProfileRequest);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/teams")
  public ResponseEntity<ResponseMessage> getAllTeam() {
    List<GetAllTeamResponse> allTeams = teamService.getAllTeam();
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, allTeams);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/team/{teamId}")
  public ResponseEntity<ResponseMessage> getOneTeam(@PathVariable("teamId") final Long teamId) {
    //처음에 httpsession 으로 세션이 있는 지 확인, 없으면 로그인 화면으로 이동,
    GetOneTeamResponse team = teamService.getOneTeam(teamId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, team);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/team/my")
  public ResponseEntity<ResponseMessage> getMyTeam(@SessionAttribute("memberId") final Long authId){
    GetOneTeamResponse team = teamService.getMyTeam(authId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, team);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @DeleteMapping("/team")
  public ResponseEntity<ResponseMessage> deleteTeam(@SessionAttribute("memberId") final Long authId) {
    teamService.deleteTeam(authId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PutMapping("/team")
  public ResponseEntity<ResponseMessage> updateTeam(
      @SessionAttribute("memberId") final Long authId,
      @RequestBody TeamProfileRequest updateTeamRequest) {
    teamService.updateTeam(authId, updateTeamRequest);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }
}
