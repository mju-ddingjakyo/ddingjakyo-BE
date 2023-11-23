package com.example.ddingjakyo_be.domain.proposal.controller.dto.request;

import com.example.ddingjakyo_be.domain.proposal.constant.ProposalStatus;
import com.example.ddingjakyo_be.domain.proposal.domain.Proposal;
import com.example.ddingjakyo_be.domain.team.domain.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchingRequest {

  private Long receiveTeamId;

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
