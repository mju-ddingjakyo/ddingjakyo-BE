package com.example.ddingjakyo_be.domain.belong.service;

import com.example.ddingjakyo_be.domain.belong.domain.Belong;
import com.example.ddingjakyo_be.domain.belong.repository.BelongRepository;
import com.example.ddingjakyo_be.domain.member.domain.Member;
import com.example.ddingjakyo_be.domain.member.service.MemberService;
import com.example.ddingjakyo_be.domain.team.domain.Team;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BelongService {

  private final BelongRepository belongRepository;

  private final MemberService memberService;

  public void doBelong(Set<Member> members, Team team) {

    for (Member member : members) {
      Belong belong = Belong.belongTo(member, team);
      belongRepository.save(belong);
    }
  }

  public void update(Set<Member> members, Team team) {
    // 해당 팀에 속한 모든 Belong 엔티티 가져오기
    List<Belong> belongs = belongRepository.findAllByTeam(team).orElseThrow(IllegalArgumentException::new);
    // 기존의 모든 Belong 삭제
    belongRepository.deleteAll(belongs);
    // 연관관계 다시 설정
    doBelong(members, team);
  }

  public Team findTeamByMemberId(Long memberId){
    Member member = memberService.findMemberById(memberId);
    Belong belong = belongRepository.findByMember(member).orElseThrow(()-> new IllegalArgumentException("이 멤버는 팀에 속하지 않았습니다."));
    return belong.getTeam();
  }
}
