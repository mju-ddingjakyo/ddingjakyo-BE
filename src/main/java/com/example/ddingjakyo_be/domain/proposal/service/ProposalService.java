package com.example.ddingjakyo_be.domain.proposal.service;

import com.example.ddingjakyo_be.common.exception.custom.EmptyException;
import com.example.ddingjakyo_be.common.exception.custom.TeamNotFoundException;
import com.example.ddingjakyo_be.common.exception.custom.UnAuthorizedException;
import com.example.ddingjakyo_be.domain.belong.service.BelongService;
import com.example.ddingjakyo_be.domain.member.controller.dto.response.MemberProfileResponse;
import com.example.ddingjakyo_be.domain.member.entity.Member;
import com.example.ddingjakyo_be.domain.proposal.constant.ProposalStatus;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.request.MatchingRequest;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.request.MatchingResultRequest;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.response.ApproveMatchingResponse;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.response.ProposalResponse;
import com.example.ddingjakyo_be.domain.proposal.controller.dto.response.ProposalsResponse;
import com.example.ddingjakyo_be.domain.proposal.entity.Proposal;
import com.example.ddingjakyo_be.domain.proposal.repository.ProposalRepository;
import com.example.ddingjakyo_be.domain.team.controller.dto.response.GetOneTeamResponse;
import com.example.ddingjakyo_be.domain.team.entity.Team;
import com.example.ddingjakyo_be.domain.team.service.TeamService;
import java.util.ArrayList;
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
    checkNotEqualGender(senderTeam, receiverTeam);
    checkEqualMemberCount(senderTeam, receiverTeam);
    checkNotEqualMember(senderTeam, receiverTeam);
    teamService.isLeader(senderTeam, authId);

    Proposal proposal = matchingRequest.toEntity(ProposalStatus.WAITING, senderTeam, receiverTeam);
    proposalRepository.save(proposal);
  }

  @Transactional(readOnly = true)
  public ProposalResponse getSendProposal(Long authId) {
    Team team = belongService.findTeamByMemberId(authId);
    Proposal proposal = proposalRepository.findBySenderTeam(team)
        .orElseThrow(() -> new EmptyException("신청한 팀이 없습니다"));
    GetOneTeamResponse getOneTeamResponse = teamService.getOneTeam(
        proposal.getReceiverTeam().getId());
    return ProposalResponse.from(proposal, getOneTeamResponse);
  }

  @Transactional(readOnly = true)
  public List<ProposalsResponse> getReceiveProposals(Long authId) {
    Team team = belongService.findTeamByMemberId(authId);
    List<Proposal> proposals = proposalRepository.findAllByReceiverTeam(team);
    checkEmpty(proposals);
    List<MemberProfileResponse> membersProfile = getMembersProfile(team);

    return proposals.stream()
        .map(proposal -> ProposalsResponse.from(proposal.getSenderTeam(), membersProfile))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ProposalsResponse> getCompleteProposals(Long authId) {
    Team team = belongService.findTeamByMemberId(authId);
    List<Proposal> proposals = proposalRepository.findAllBySenderTeamOrReceiverTeamAndProposalStatus(team, team, ProposalStatus.APPROVED);
    checkEmpty(proposals);

    return getProposalsResponses(proposals, team);
  }

  public ApproveMatchingResponse approveMatching(MatchingResultRequest matchingResultRequest,
      Long authId) {
    Team authTeam = belongService.findTeamByMemberId(authId);
    teamService.isLeader(authTeam, authId);

    Team team = teamService.findTeamById(matchingResultRequest.getSendTeamId());
    Proposal proposal = proposalRepository.findBySenderTeam(team)
        .orElseThrow(() -> new TeamNotFoundException("매칭 수락한 팀이 조회되지 않습니다."));
    proposal.approveProposal();
    proposal.getReceiverTeam().completeMatching();
    proposal.getSenderTeam().completeMatching();
    return ApproveMatchingResponse.from(proposal);
  }

  public void rejectMatching(MatchingResultRequest matchingResultRequest, Long authId) {
    Team authTeam = belongService.findTeamByMemberId(authId);
    teamService.isLeader(authTeam, authId);

    Team senderTeam = teamService.findTeamById(matchingResultRequest.getSendTeamId());
    Proposal proposal = proposalRepository.findBySenderTeam(senderTeam)
        .orElseThrow(() -> new TeamNotFoundException("매칭 거부한 팀이 조회되지 않습니다."));
    proposal.rejectProposal();
  }

  private List<MemberProfileResponse> getMembersProfile(Team team) {
    List<Member> members = teamService.findMembersByTeam(team);
    return members.stream()
        .map(MemberProfileResponse::from)
        .toList();
  }

  private List<ProposalsResponse> getProposalsResponses(List<Proposal> proposals, Team team) {
    List<ProposalsResponse> proposalsResponses = new ArrayList<>();
    proposals.stream()
        .filter(proposal -> team.equals(proposal.getSenderTeam()))
        .forEach(proposal -> proposalsResponses.add(ProposalsResponse.from(proposal.getReceiverTeam(), getMembersProfile(proposal.getReceiverTeam()))));
    proposals.stream()
        .filter(proposal -> team.equals(proposal.getReceiverTeam()))
        .forEach(proposal -> proposalsResponses.add(ProposalsResponse.from(proposal.getSenderTeam(), getMembersProfile(proposal.getSenderTeam()))));
    return proposalsResponses;
  }

  private void isProposal(Team senderTeam) {
    proposalRepository.findBySenderTeam(senderTeam).ifPresent(team -> {
      throw new UnAuthorizedException("매칭 신청은 동시에 한 팀만 가능합니다");
    });
  }

  private void checkEqualMemberCount(Team senderTeam, Team receiverTeam) {
    if (!Objects.equals(senderTeam.getMemberCount(), receiverTeam.getMemberCount())) {
      throw new UnAuthorizedException("같은 인원 수의 팀에만 신청 가능합니다.");
    }
  }

  private void checkEqualGender(Team senderTeam, Team receiverTeam) {
    if (Objects.equals(senderTeam.getGender(), receiverTeam.getGender())) {
      throw new UnAuthorizedException("다른 성별의 팀에만 신청 가능합니다.");
    }
  }

  private void checkNotEqualMember(Team senderTeam, Team receiverTeam) {
    List<Member> sendMembers = teamService.findMembersByTeam(senderTeam);
    List<Member> receiveMembers = teamService.findMembersByTeam(receiverTeam);
    boolean isNotDuplicate = sendMembers.stream()
        .filter(receiveMembers::contains)
        .toList().isEmpty();
    if(!isNotDuplicate){
      throw new IllegalArgumentException("매칭 팀 중 같은 멤버가 존재합니다.");
    };
  }

  private void checkEmpty(List<Proposal> proposals) {
    if (proposals.isEmpty()) {
      throw new EmptyException("팀이 조회되지 않습니다.");
    }
  }
}
