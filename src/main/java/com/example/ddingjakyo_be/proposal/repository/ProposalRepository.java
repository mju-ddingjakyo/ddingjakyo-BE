package com.example.ddingjakyo_be.proposal.repository;

import com.example.ddingjakyo_be.proposal.domain.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

}
