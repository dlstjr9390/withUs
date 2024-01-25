package com.withus.withus.club.repository;

import com.withus.withus.club.entity.ClubMember;

import com.withus.withus.club.entity.ClubMemberRole;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long>, ClubMemberRepositoryQuery {

    Optional<ClubMember> findClubMemberByMemberIdAndClubId(Long memberId, Long clubId);

    Page<ClubMember> findByMemberIdAndClub_IsActive(Long member_id, Pageable pageable, boolean isActive);

    Page<ClubMember> findByMemberIdAndClubMemberRole(Long memberId, ClubMemberRole role, Pageable pageable);

    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);

    List<ClubMember> findByClubId(Long clubId);

    //  추가
    Integer countByClubId(Long clubId);
}
