package com.withus.withus.domain.chat.controller;

import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.exception.BisException;
import com.withus.withus.global.response.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatViewController {

  @GetMapping("/chatRoom/{roomId}/chatRoomPage")
  public String chatRoomPage(
      @PathVariable("roomId") Long roomId,
      Model model,
      @AuthMember Member member
  ) {
    model.addAttribute("roomId", roomId);
    model.addAttribute("memberName", member.getUsername());
    model.addAttribute("memberId", member.getId());

    return "chat/chatRoom";
  }

  @GetMapping("/member/{memberId}/chatRoomListPage")
  public String chatRoomListPage(
      @PathVariable("memberId") Long memberId,
      Model model,
      @AuthMember Member member
  ) {
    if (!member.getId().equals(memberId)) {
      throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
    }

    model.addAttribute("memberId", memberId);

    return "chat/chatRoomList";
  }
}
