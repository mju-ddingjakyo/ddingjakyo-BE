package com.example.ddingjakyo_be.proposal.service;

import com.example.ddingjakyo_be.member.service.MemberService;
import com.example.ddingjakyo_be.proposal.constant.ProposalStatus;
import com.example.ddingjakyo_be.proposal.controller.dto.request.MatchingRequest;
import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.proposal.repository.ProposalRepository;
import com.example.ddingjakyo_be.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProposalService {

  private final ProposalRepository proposalRepository;

  private final MemberService memberService;
  public void proposeMatching(Long receiverId, MatchingRequest matchingRequest) {
    //httpSession으로 getsession을 한다음 user의 정보를 얻어온다. userid를 얻는다.
    Team senderTeam = memberService.findTeamByMemberId(senderId);
    Team receiverTeam = memberService.findTeamByMemberId(receiverId);

    Proposal proposal = matchingRequest.toEntity(ProposalStatus.WAITING, senderTeam, receiverTeam);
    proposalRepository.save(proposal);
  }
}
