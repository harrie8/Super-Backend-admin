package com.sppart.admin.member.service;

import com.sppart.admin.member.domain.mapper.MemberMapper;
import com.sppart.admin.member.dto.ResponseMembers;
import com.sppart.admin.utils.FilterType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberMapper memberMapper;

    @Override
    public ResponseEntity<?> getAllMembers(Date startDate, Date endDate, boolean isAuthor, String searchString, FilterType filterType, int page) {
        memberMapper.getAllMembers();
        return null;
    }
}
