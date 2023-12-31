package com.example.ddingjakyo_be.domain.proposal.controller;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.response.ProposalResponse;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.response.ProposalsResponse;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.request.MatchingRequest;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.request.MatchingResultRequest;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.response.ApproveMatchingResponse;
import com.example.ddingjakyo_be.domain.proposal.service.ProposalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProposalController {

  private final ProposalService proposalService;

  @PostMapping("/proposal")
  public ResponseEntity<ResponseMessage> proposeMatching(@SessionAttribute("memberId") Long authId, @RequestBody MatchingRequest matchingRequest) {
    proposalService.proposeMatching(authId, matchingRequest);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/proposal/send-proposal")
  public ResponseEntity<ResponseMessage> getSendProposal(@SessionAttribute("memberId") Long authId) {
    ProposalResponse proposalResponse = proposalService.getSendProposal(authId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, proposalResponse);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/proposal/receive-proposals")
  public ResponseEntity<ResponseMessage> getReceiveProposals(@SessionAttribute("memberId") Long authId) {
    List<ProposalsResponse> receiveProposals = proposalService.getReceiveProposals(authId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, receiveProposals);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @GetMapping("/proposal/complete-proposal")
  public ResponseEntity<ResponseMessage> getCompleteProposals(@SessionAttribute("memberId") Long authId){
    List<ProposalsResponse> completeProposals = proposalService.getCompleteProposals(authId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK, completeProposals);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }

  @PatchMapping("/proposal")
  public ResponseEntity<ResponseMessage> matchingResult(@SessionAttribute("memberId") Long authId, @RequestBody MatchingResultRequest matchingResultRequest) {
    //프론트에서 수락한 팀 아이디를 받아온다.
    //수락, 거절 상태도 받아온다.
    if (matchingResultRequest.isMatchingResult()) {
      ApproveMatchingResponse approveMatchingResponse = proposalService.approveMatching(
          matchingResultRequest, authId);
      ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK,
          approveMatchingResponse);
      return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    proposalService.rejectMatching(matchingResultRequest, authId);
    ResponseMessage responseMessage = ResponseMessage.of(ResponseStatus.OK);
    return new ResponseEntity<>(responseMessage, HttpStatus.OK);
  }
}
