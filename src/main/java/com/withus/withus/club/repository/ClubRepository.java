package com.withus.withus.club.repository;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.entity.Club;
import com.withus.withus.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ClubRepository extends JpaRepository<Club, Long>, ClubRepositoryQuery{

    Optional<Club> findByIsActiveAndId(boolean isActive, Long clubId);
  
    boolean existsByIsActiveAndId(boolean isActive, Long clubId);

    Page<Club> findByCategoryAndIsActive(ClubCategory category, boolean isActive, Pageable pageable);

    Page<Club> findAllByIsActive(boolean isActive, Pageable pageable);

    Integer countByIsActive(boolean isActive);


    //삭제 스캐줄러용
    List<Club> findAllByIsActive(boolean isActive);
    List<Club> findAllByMemberId(Long memberId);
}

