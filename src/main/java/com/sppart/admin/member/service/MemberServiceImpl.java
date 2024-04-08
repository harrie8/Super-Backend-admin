package com.sppart.admin.member.service;

import com.sppart.admin.member.domain.entity.Member;
import com.sppart.admin.member.domain.mapper.MemberMapper;
import com.sppart.admin.member.dto.ResponseMembers;
import com.sppart.admin.utils.FilterType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberMapper memberMapper;

    @Override
    public ResponseEntity<?> getAllMembers(Date startDate, Date endDate, boolean isAuthor, String searchString, FilterType filterType, int page) {
        return ResponseEntity.ok(getListByFilter(filterType, searchString));
    }

    private List<?> getListByFilter(FilterType filterType, String searchString){
        switch (filterType){
            case all -> {
                return entityToResponse(memberMapper.getAllMembers());
            }
            case email -> {
                return entityToResponse(memberMapper.getMembersByEmail(searchString));
            }
            case name -> {
                return entityToResponse(memberMapper.getMembersByName(searchString));
            }
            default -> {
                log.error("[{}] Parsing Error.. Wrong Filter Type.", getClass().getSimpleName());
                throw new RuntimeException();
            }
        }
    }

    private List<ResponseMembers> entityToResponse(List<Member> members){
        List<ResponseMembers> memberList = new ArrayList<>(members.size());
        members.forEach(member -> memberList.add(ResponseMembers.builder().build()));
        return memberList;
    }
}
