package com.example.ddingjakyo_be.proposal.service;

import com.example.ddingjakyo_be.belong.service.BelongService;
import com.example.ddingjakyo_be.common.exception.NoAuthException;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.proposal.constant.ProposalStatus;
import com.example.ddingjakyo_be.proposal.controller.dto.request.MatchingRequest;
import com.example.ddingjakyo_be.proposal.controller.dto.request.MatchingResultRequest;
import com.example.ddingjakyo_be.proposal.controller.dto.response.ApproveMatchingResponse;
import com.example.ddingjakyo_be.proposal.controller.dto.response.ReceiveProposalResponse;
import com.example.ddingjakyo_be.proposal.controller.dto.response.SendProposalResponse;
import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.proposal.repository.ProposalRepository;
import com.example.ddingjakyo_be.team.controller.dto.response.GetOneTeamResponse;
import com.example.ddingjakyo_be.team.domain.Team;
import com.example.ddingjakyo_be.team.service.TeamService;
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

  public void proposeMatching(Long authId, MatchingRequest matchingRequest) {
    isProposal(authId);
    Team senderTeam = belongService.findTeamByMemberId(authId);
    Team receiverTeam = teamService.findTeamById(matchingRequest.getReceiveTeamId());

    teamService.isLeader(senderTeam, authId);
    Proposal proposal = matchingRequest.toEntity(ProposalStatus.WAITING, senderTeam, receiverTeam);
    proposalRepository.save(proposal);
  }

  public SendProposalResponse getSendProposal(Long authId) {
    Team team = belongService.findTeamByMemberId(authId);
    Proposal proposal = proposalRepository.findBySenderTeam(team).orElseThrow(IllegalArgumentException::new);
    GetOneTeamResponse getOneTeamResponse = teamService.getOneTeam(proposal.getReceiverTeam().getId());
    return SendProposalResponse.from(proposal, getOneTeamResponse);
  }

  public List<ReceiveProposalResponse> getReceiveProposals(Long authId) {
    Team team = belongService.findTeamByMemberId(authId);
    List<Proposal> proposals = proposalRepository.findAllByReceiverTeam(team).orElseThrow(IllegalArgumentException::new);
    List<MemberProfileResponse> membersProfile = getMembersProfile(team);

    return proposals.stream()
        .map(proposal -> ReceiveProposalResponse.from(proposal, membersProfile))
        .toList();
  }

  public ApproveMatchingResponse approveMatching(MatchingResultRequest matchingResultRequest) {
    Team team = teamService.findTeamById(matchingResultRequest.getSendTeamId());
    teamService.isLeader(team, matchingResultRequest.getSendTeamId());
    Proposal proposal = proposalRepository.findBySenderTeam(team).orElseThrow(IllegalArgumentException::new);
    proposal.approveProposal();
    proposal.getReceiverTeam().completeMatching();
    proposal.getSenderTeam().completeMatching();
    return ApproveMatchingResponse.from(proposal);
  }

  public void rejectMatching(MatchingResultRequest matchingResultRequest) {
    Team senderTeam = teamService.findTeamById(matchingResultRequest.getSendTeamId());
    Proposal proposal = proposalRepository.findBySenderTeam(senderTeam).orElseThrow(IllegalArgumentException::new);
    proposal.rejectProposal();
  }

  private List<MemberProfileResponse> getMembersProfile(Team team) {
    List<Member> members = teamService.findMembersByTeam(team);
    return members.stream()
        .map(MemberProfileResponse::from)
        .toList();
  }

  private void isProposal(Long authId) {
    Team authTeam = belongService.findTeamByMemberId(authId);
    proposalRepository.findBySenderTeam(authTeam).ifPresent(team->{throw new NoAuthException();});
  }
}
