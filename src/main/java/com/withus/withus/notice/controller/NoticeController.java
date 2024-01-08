package com.withus.withus.notice.controller;


import com.withus.withus.global.response.ResponseCode;

import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import com.withus.withus.notice.service.NoticeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
  private final NoticeService noticeService;


  @PostMapping("/{clubId}")
  public ResponseEntity<CommonResponse> createNotice(@PathVariable Long clubId, @RequestBody
      NoticeRequestDto requestDto){
    NoticeResponseDto responseDto = noticeService.createNotice(clubId,requestDto);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_CREATE.getHttpStatus()).body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_CREATE,responseDto));
  }
}
