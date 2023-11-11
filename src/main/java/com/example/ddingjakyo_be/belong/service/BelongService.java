package com.example.ddingjakyo_be.belong.service;

import com.example.ddingjakyo_be.belong.domain.Belong;
import com.example.ddingjakyo_be.belong.repository.BelongRepository;
import com.example.ddingjakyo_be.member.domain.Member;
import com.example.ddingjakyo_be.team.domain.Team;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BelongService {

  private final BelongRepository belongRepository;

  public void doBelong(List<Member> members, Team team) {

    for (Member member : members) {
      Belong belong = Belong.belongTo(member, team);
      belongRepository.save(belong);
    }

  }

  public void update(List<Member> members, Team team) {
    // 해당 팀에 속한 모든 Belong 엔티티 가져오기
    List<Belong> belongs = belongRepository.findByTeam(team);
    // 기존의 모든 Belong 삭제
    belongRepository.deleteAll(belongs);
    // 연관관계 다시 설정
    doBelong(members, team);
  }
}
