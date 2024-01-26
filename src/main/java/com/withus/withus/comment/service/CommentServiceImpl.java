package com.withus.withus.comment.service;

import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.entity.ClubMemberRole;
import com.withus.withus.club.service.ClubMemberServiceImpl;
import com.withus.withus.comment.dto.CommentDeleteRequestDto;
import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.comment.dto.ReportRequestDto;
import com.withus.withus.comment.entity.Comment;
import com.withus.withus.comment.entity.ReportComment;
import com.withus.withus.comment.repository.CommentRepository;
import com.withus.withus.comment.repository.CommentReportRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.service.NoticeServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final NoticeServiceImpl noticeService;
    private final ClubMemberServiceImpl clubMemberService;
    private final CommentReportRepository commentReportRepository;

    @Override
    public CommentResponseDto createComment(Long noticeId, CommentRequestDto commentRequestDto, Member member) {
        Notice notice = noticeService.findByIsActiveAndNoticeId(noticeId);
        if (!clubMemberService.existsClubMemberByMemberIdAndClubId(member.getId(), notice.getClub().getId())) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB_MEMBER_EXIST);
        }
        Comment savedComment = commentRepository.save(Comment.createComment(commentRequestDto, member, notice));
        return CommentResponseDto.createCommentResponseDto(savedComment);
    }

    @Transactional
    @Override
    public CommentResponseDto updateComment(Long noticeId, Long commentId, CommentRequestDto commentRequestDto, Member member) {
        Comment comment = findByIsActiveAndCommentId(commentId);
        if(clubMemberService.existHost(member.getId(), commentRequestDto.clubId()) || comment.getMember().getId().equals(member.getId())){
            comment.update(commentRequestDto);
            return CommentResponseDto.createCommentResponseDto(comment);
        }
        else {
            throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
        }

    }

    @Override
    public List<CommentResponseDto> getComment(Long noticeId, PageableDto pageableDto) {
        List<Comment> commentList = commentRepository
                .findAllByIsActiveAndNoticeId(true,noticeId, PageableDto
                        .getsPageableDto(
                                pageableDto.page(),
                                pageableDto.size(),
                                pageableDto.sortBy()
                        ).toPageable()
                );
        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            responseDtoList.add(CommentResponseDto.createCommentResponseDto(comment));
        }
        return responseDtoList;
    }

    @Transactional
    @Override
    public void deleteComment(Long noticeId, Long commentId, Member loginMember, CommentDeleteRequestDto commentDeleteRequestDto) {
        Comment comment = findByIsActiveAndCommentId(commentId);
        if(clubMemberService.existHost(loginMember.getId(), commentDeleteRequestDto.clubId()) || comment.getMember().getId().equals(loginMember.getId())){
            comment.inActive();
        }
        else {
            throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
        }

    }

    @Transactional
    @Override
    public void createReportComment(Long commentId, ReportRequestDto requestDto, Member member) {
        Comment comment = findByIsActiveAndCommentId(commentId);
        if (!commentReportRepository.existsByCommentIdAndMemberId(comment.getId(), member.getId())) {
            commentReportRepository.save(ReportComment.createReport(requestDto, member, comment));
            if (commentReportRepository.countByCommentId(comment.getId()) > 5) {
                comment.inActive();
            }
        } else {
            throw new BisException(ErrorCode.COMMENT_EXIST_REPORT);
        }
    }

    @Override
    public Integer count(Long noticeId) {
        return commentRepository.countByIsActiveAndNoticeId(true, noticeId);
    }


    private boolean existsByNoticeId(Long noticeId) {
        return noticeService.existByIsActiveAndNoticeId(noticeId);
    }

    private Comment findByIsActiveAndCommentId(Long commentId) {
        Comment comment = commentRepository.findByIsActiveAndId(true, commentId)
                .orElseThrow(() ->
                        new BisException(ErrorCode.NOT_FOUND_COMMENT)
                );
        return comment;
    }
}
