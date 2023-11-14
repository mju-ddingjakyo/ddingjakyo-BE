package com.example.ddingjakyo_be.proposal.repository;

import com.example.ddingjakyo_be.proposal.domain.Proposal;
import com.example.ddingjakyo_be.team.domain.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

  Optional<Proposal> findBySenderTeam(Team team);
  Optional<List<Proposal>> findAllByReceiverTeam(Team team);
}
