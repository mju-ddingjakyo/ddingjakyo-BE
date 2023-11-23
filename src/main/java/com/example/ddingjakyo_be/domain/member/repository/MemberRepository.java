package com.example.ddingjakyo_be.domain.member.repository;

import com.example.ddingjakyo_be.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findMemberByEmail(String email);
}
