package com.example.ddingjakyo_be.member.repository;

import com.example.ddingjakyo_be.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findMemberByEmail(String email);
}
