package com.example.ddingjakyo_be.domain.team.repository;

import com.example.ddingjakyo_be.domain.team.domain.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

  Optional<Team> findByLeaderId(Long leaderId);
}
