package com.example.ddingjakyo_be.domain.proposal.repository;

import com.example.ddingjakyo_be.domain.proposal.constant.ProposalStatus;
import com.example.ddingjakyo_be.domain.proposal.entity.Proposal;
import com.example.ddingjakyo_be.domain.team.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

  Optional<Proposal> findBySenderTeam(Team team);
  List<Proposal> findAllByReceiverTeam(Team team);

  List<Proposal> findAllBySenderTeamOrReceiverTeamAndProposalStatus(Team senderTeam,
      Team receiverTeam, ProposalStatus proposalStatus);
}
