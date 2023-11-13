package com.example.ddingjakyo_be.proposal.service;

import com.example.ddingjakyo_be.belong.service.BelongService;
import com.example.ddingjakyo_be.member.service.MemberService;
import com.example.ddingjakyo_be.proposal.constant.ProposalStatus;
import com.example.ddingjakyo_be.proposal.controller.dto.request.MatchingRequest;
import com.example.ddingjakyo_be.proposal.controller.dto.request.MatchingResultRequest;
import com.example.ddingjakyo_be.proposal.controller.dto.response.ApproveMatchingResponse;
import com.example.ddingjakyo_be.proposal.controller.dto.response.ReceiveProposalResponse;
import com.example.ddingjakyo_be.proposal.controller.dto.response.SendProposalResponse;
import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.proposal.repository.ProposalRepository;
import com.example.ddingjakyo_be.team.domain.Team;
import com.example.ddingjakyo_be.team.service.TeamService;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProposalService {

  private final ProposalRepository proposalRepository;

  private final BelongService belongService;

  private final TeamService teamService;

  public void proposeMatching(MatchingRequest matchingRequest) {
    //httpSession으로 getsession을 한다음 user의 정보를 얻어온다. userid를 얻는다.
    Team senderTeam = belongService.findTeamByMemberId(senderId);
    Team receiverTeam = belongService.findTeamByMemberId(matchingRequest.getReceiverId());

    Proposal proposal = matchingRequest.toEntity(ProposalStatus.WAITING, senderTeam, receiverTeam);
    proposalRepository.save(proposal);
  }

  public SendProposalResponse getSendProposal(Long teamId) {
    Proposal proposal = getProposalFromSenderTeam(teamId);
    return SendProposalResponse.from(proposal);
  }

  public List<ReceiveProposalResponse> getReceiveProposals(Long teamId) {
    Team team = teamService.findTeamById(teamId);
    List<Proposal> proposals = proposalRepository.findAllByReceiverTeam(team);
    return proposals.stream()
        .map(ReceiveProposalResponse::from)
        .toList();
  }

  public ApproveMatchingResponse approveMatching(MatchingResultRequest matchingResultRequest) {
    Proposal proposal = getProposalFromSenderTeam(matchingResultRequest.getSenderId());
    proposal.approveProposal();
    proposal.getReceiverTeam().completeMatching();
    proposal.getSenderTeam().completeMatching();
    return ApproveMatchingResponse.from(proposal);
  }

  public void rejectMatching(MatchingResultRequest matchingResultRequest) {
    Proposal proposal = getProposalFromSenderTeam(matchingResultRequest.getSenderId());
    proposal.rejectProposal();
  }

  private Proposal getProposalFromSenderTeam(Long teamId) {
    Team senderTeam = teamService.findTeamById(teamId);
    return proposalRepository.findBySenderTeam(senderTeam);
  }
}
