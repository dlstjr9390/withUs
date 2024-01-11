package com.withus.withus.global.security;

import com.withus.withus.member.entity.Member;
import com.withus.withus.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final MemberRepository memberRepository;

  public UserDetailsServiceImpl(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String loginname) throws UsernameNotFoundException {
    Member member = memberRepository.findMemberByLoginnameAndIsActive(loginname, true)
        .orElseThrow(() -> new UsernameNotFoundException("Not Found " + loginname));

    return new UserDetailsImpl(member);
  }
}