package com.example.ddingjakyo_be.belong.repository;

import com.example.ddingjakyo_be.belong.domain.Belong;
import com.example.ddingjakyo_be.team.domain.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BelongRepository extends JpaRepository<Belong, Long> {

  List<Belong> findByTeam(Team team);
}
