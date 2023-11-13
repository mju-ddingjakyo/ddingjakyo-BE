package com.example.ddingjakyo_be.proposal.controller;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import com.example.ddingjakyo_be.proposal.controller.dto.request.MatchingRequest;
import com.example.ddingjakyo_be.proposal.controller.dto.response.SendProposalResponse;
import com.example.ddingjakyo_be.proposal.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProposalController {

  private final ProposalService proposalService;

  @PostMapping("/proposal/{receiverid}")
  public ResponseEntity<ResponseMessage> proposeMatching(@PathVariable("receiverid") Long receiverId, @RequestBody MatchingRequest matchingRequest) {
    proposalService.proposeMatching(receiverId, matchingRequest);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/proposal/send-proposal")
  public ResponseEntity<ResponseMessage> getSendProposal(Long teamId) {
    //세션 -> 유저 정보 -> 팀 아이디
    SendProposalResponse sendProposalResponse = proposalService.getSendProposal(teamId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, sendProposalResponse);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/proposal/receive-proposals")
  public ResponseEntity<ResponseMessage> getReceiveProposals(Long teamId) {
    //세션 -> 유저 정보 -> 팀 아이디
    List<ReceiveProposalResponse> receiveProposals = proposalService.getReceiveProposals(teamId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, receiveProposals);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }
  @PatchMapping("/proposal")
  public ResponseEntity<ResponseMessage> matchingResult(@RequestBody MatchingResultRequest matchingResultRequest){
    //프론트에서 수락한 팀 아이디를 받아온다.
    //수락, 거절 상태도 받아온다.
    if(matchingResultRequest.isMatchingResult()) {
      ApproveMatchingResponse approveMatchingResponse = proposalService.approveMatching(matchingResultRequest);
      ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, approveMatchingResponse);
      return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    proposalService.rejectMatching(matchingResultRequest);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }
}
