package com.withus.withus.notice.service;


import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
  private final NoticeRepository noticeRepository;


  @Override
  public NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto) {
    Notice saveNotice = noticeRepository.save(Notice.createNotice(requestDto));
    return NoticeResponseDto.createNoticeResponseDto(saveNotice);
  }

  @Transactional
  @Override
  public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto) {
    Notice notice = findById(noticeId);
    notice.update(requestDto);
    return NoticeResponseDto.createNoticeResponseDto(notice);
  }

  public Notice findById(Long noticeId) {
    Notice notice = noticeRepository.findById(noticeId).orElseThrow(()-> new BisException(ErrorCode.NOT_FOUND_NOTICE));
    return notice;
  }
}
