package com.withus.withus.club.service;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.dto.ReportClubRequestDto;
import com.withus.withus.club.dto.ReportClubResponseDto;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.entity.ClubMemberRole;
import com.withus.withus.club.entity.ReportClub;
import com.withus.withus.club.repository.ClubRepository;
import com.withus.withus.club.repository.ReportClubRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final ReportClubRepository reportClubRepository;

    // 작성
    @Override
    public ClubResponseDto createClub(ClubRequestDto clubRequestDto, Member member) {
        LocalDateTime startTime = clubRequestDto.startTime();
        LocalDateTime endTime = clubRequestDto.endTime();
        Club club = Club.createClub(clubRequestDto, member, startTime, endTime);
        Club savedClub = clubRepository.save(club);
        return ClubResponseDto.createClubResponseDto(savedClub);
    }

    //조회
    @Override
    public ClubResponseDto getClub(Long clubId) {
        Club club = findClubById(clubId);
        return ClubResponseDto.createClubResponseDto(club);
    }

    @Transactional
    public ClubResponseDto updateClub(Long clubId, ClubRequestDto clubRequestDto, Member member) {
        Club club = verifyMember(clubId);
        club.update(clubRequestDto);
        return ClubResponseDto.createClubResponseDto(club);
    }

    @Override
    @Transactional
    public String deleteClub(Long clubId, Member member) {
        Club club = verifyMember(clubId);
        club.delete();
        return "Club delete successfully";
    }

    @Override
    public void updateReportClub(Long clubId) {

    }

    public ReportClubResponseDto createReportClub(Long clubId, ReportClubRequestDto reportClubRequestDto, Member member) {
        Club club = verifyMember(clubId);
        ReportClub reportClub = ReportClub.createReport(reportClubRequestDto, member, club);
        if (!reportClubRepository.existsByClubIdAndMemberId(club.getId(), member.getId())) {
            reportClubRepository.save(reportClub);
            if (reportClubRepository.countByClubId(club.getId()) > 5) {
                club.isActive();
            }
            return ReportClubResponseDto.createReportClubResponseDto(club, reportClub);
        } else {
            throw new BisException(ErrorCode.CLUB_EXIST_REPORT);
        }
    }

    @Override
    public List<ClubResponseDto> getsClubByCategory(ClubCategory category, PageableDto pageableDto) {
        List<Club> clubList = clubRepository.
                findByCategoryAndIsActive(category, true, PageableDto.getsPageableDto(
                                pageableDto.page(),
                                pageableDto.size(),
                                pageableDto.sortBy()
                        ).toPageable()
                );
        return clubList.stream()
                .map(ClubResponseDto::createClubResponseDto)
                .collect(Collectors.toList());
    }
    public Club findClubById(Long clubId) {
        return clubRepository.findById(clubId).
                orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_CLUB));

    }

    private Club verifyMember(Long clubId) {
        Club club = clubRepository.findByIsActiveAndId(true, clubId)
                .orElseThrow(() ->
                        new BisException(ErrorCode.NOT_FOUND_CLUB)
                );
        return club;
    }

    public Club findByIsActiveAndClubId(Long clubId) {
        Club club = clubRepository.findByIsActiveAndId(true, clubId)
                .orElseThrow(() ->
                        new BisException(ErrorCode.NOT_FOUND_CLUB)
                );
        return club;
    }

    public boolean existByIsActiveAndClubId(Long clubId){
        return clubRepository.existsByIsActiveAndId(true,clubId);
    }
}
