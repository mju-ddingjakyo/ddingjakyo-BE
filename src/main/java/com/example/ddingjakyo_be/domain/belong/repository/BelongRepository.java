package com.example.ddingjakyo_be.domain.belong.repository;

import com.example.ddingjakyo_be.domain.belong.domain.Belong;
import com.example.ddingjakyo_be.domain.member.domain.Member;
import com.example.ddingjakyo_be.domain.team.domain.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BelongRepository extends JpaRepository<Belong, Long> {

  Optional<List<Belong>> findAllByTeam(Team team);
  Optional<Belong> findByMember(Member member);
}
