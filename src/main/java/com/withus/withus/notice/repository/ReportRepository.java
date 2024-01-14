package com.withus.withus.notice.repository;


import com.withus.withus.notice.entity.ReportNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportNotice,Long> {
  boolean existsByNoticeIdAndMemberId(Long noticeId, Long memberId);

  int countByNoticeId(Long noticeId);

}
