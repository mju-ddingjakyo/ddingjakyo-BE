package com.example.ddingjakyo_be.proposal.controller.dto.request;

import com.example.ddingjakyo_be.proposal.constant.ProposalStatus;
import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.team.domain.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchingRequest {

  private String kakaoRoomURL;

  public Proposal toEntity(ProposalStatus proposalStatus, Team senderTeam, Team receiverTeam){
    return Proposal.builder()
        .kakaoRoomURL(kakaoRoomURL)
        .proposalStatus(proposalStatus)
        .senderTeam(senderTeam)
        .receiverTeam(receiverTeam)
        .build();
  }
}
