package com.example.ddingjakyo_be.proposal.service;

import com.example.ddingjakyo_be.belong.service.BelongService;
import com.example.ddingjakyo_be.common.exception.NoAuthException;
import com.example.ddingjakyo_be.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.proposal.constant.ProposalStatus;
import com.example.ddingjakyo_be.proposal.controller.dto.request.MatchingRequest;
import com.example.ddingjakyo_be.proposal.controller.dto.request.MatchingResultRequest;
import com.example.ddingjakyo_be.proposal.controller.dto.response.ApproveMatchingResponse;
import com.example.ddingjakyo_be.proposal.controller.dto.response.ProposalResponse;
import com.example.ddingjakyo_be.proposal.controller.dto.response.ProposalsResponse;
import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.proposal.repository.ProposalRepository;
import com.example.ddingjakyo_be.team.controller.dto.response.GetOneTeamResponse;
import com.example.ddingjakyo_be.team.domain.Team;
import com.example.ddingjakyo_be.team.service.TeamService;
import java.util.List;
import java.util.Objects;
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
    Team senderTeam = belongService.findTeamByMemberId(authId);
    Team receiverTeam = teamService.findTeamById(matchingRequest.getReceiveTeamId());

    isProposal(senderTeam);
    checkEqualGender(senderTeam, receiverTeam);
    checkEqualMemberCount(senderTeam, receiverTeam);
    teamService.isLeader(senderTeam, authId);

    Proposal proposal = matchingRequest.toEntity(ProposalStatus.WAITING, senderTeam, receiverTeam);
    proposalRepository.save(proposal);
  }

  public ProposalResponse getSendProposal(Long authId) {
    Team team = belongService.findTeamByMemberId(authId);
    Proposal proposal = proposalRepository.findBySenderTeam(team)
        .orElseThrow(() -> new IllegalArgumentException("신청한 팀이 없습니다"));
    GetOneTeamResponse getOneTeamResponse = teamService.getOneTeam(proposal.getReceiverTeam().getId());
    return ProposalResponse.from(proposal, getOneTeamResponse);
  }

  public List<ProposalsResponse> getReceiveProposals(Long authId) {
    Team team = belongService.findTeamByMemberId(authId);
    List<Proposal> proposals = proposalRepository.findAllByReceiverTeam(team);
    checkEmpty(proposals);
    List<MemberProfileResponse> membersProfile = getMembersProfile(team);

    return proposals.stream()
        .map(proposal -> ProposalsResponse.from(proposal, membersProfile))
        .toList();
  }

  public List<ProposalsResponse> getCompleteProposals(Long authId) {
    Team team = belongService.findTeamByMemberId(authId);
    List<Proposal> proposals = proposalRepository.findAllBySenderTeamOrReceiverTeamAndProposalStatus(team, team, ProposalStatus.APPROVED);
    checkEmpty(proposals);
    List<MemberProfileResponse> membersProfile = getMembersProfile(team);

    return proposals.stream()
        .map(proposal -> ProposalsResponse.from(proposal, membersProfile))
        .toList();
  }

  public ApproveMatchingResponse approveMatching(MatchingResultRequest matchingResultRequest) {
    Team team = teamService.findTeamById(matchingResultRequest.getSendTeamId());
    teamService.isLeader(team, matchingResultRequest.getSendTeamId());
    Proposal proposal = proposalRepository.findBySenderTeam(team)
        .orElseThrow(IllegalArgumentException::new);
    proposal.approveProposal();
    proposal.getReceiverTeam().completeMatching();
    proposal.getSenderTeam().completeMatching();
    return ApproveMatchingResponse.from(proposal);
  }

  public void rejectMatching(MatchingResultRequest matchingResultRequest) {
    Team senderTeam = teamService.findTeamById(matchingResultRequest.getSendTeamId());
    Proposal proposal = proposalRepository.findBySenderTeam(senderTeam)
        .orElseThrow(IllegalArgumentException::new);
    proposal.rejectProposal();
  }

  private List<MemberProfileResponse> getMembersProfile(Team team) {
    List<Member> members = teamService.findMembersByTeam(team);
    return members.stream()
        .map(MemberProfileResponse::from)
        .toList();
  }

  private void isProposal(Team senderTeam) {
    proposalRepository.findBySenderTeam(senderTeam).ifPresent(team -> {throw new NoAuthException("매칭 신청은 동시에 한 팀만 가능합니다");});
  }

  private void checkEqualMemberCount(Team senderTeam, Team receiverTeam) {
    if (!Objects.equals(senderTeam.getMemberCount(), receiverTeam.getMemberCount())) {
      throw new NoAuthException("같은 인원 수의 팀에만 신청 가능합니다.");
    }
  }

  private void checkEqualGender(Team senderTeam, Team receiverTeam) {
    if (!Objects.equals(senderTeam.getGender(), receiverTeam.getGender())) {
      throw new NoAuthException("다른 성별의 팀에만 신청 가능합니다.");
    }
  }

  private void checkEmpty(List<Proposal> proposals) {
    if(proposals.isEmpty()){
      throw new IllegalArgumentException("팀이 조회되지 않습니다.");
    }
  }
}
