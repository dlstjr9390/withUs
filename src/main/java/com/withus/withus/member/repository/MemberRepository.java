package com.withus.withus.member.repository;

import com.withus.withus.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByLoginname(String loginname);

  boolean existsUserByEmail(String email);

  boolean existsUserByLoginname(String loginname);

  boolean existsUserByUsername(String username);

  Optional<Member> findByIsActiveAndId(Boolean isActive, Long memberId);

  boolean existsByIsActiveAndId(Boolean isActive, Long memberId);
}
