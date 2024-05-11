package com.sppart.admin.member.service;

import com.sppart.admin.member.dto.ResponseMemberDetail;
import com.sppart.admin.member.dto.ResponseMembers;
import com.sppart.admin.member.dto.UpdateUserInfo;
import com.sppart.admin.utils.FilterType;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface MemberService {
    ResponseMembers getAllMembers(Date startDate, Date endDate, boolean isAuthor, String searchString, FilterType filterType, int page);
    ResponseMemberDetail getMemberByEmail(String email);
    ResponseMembers getDeleteMembers(Date startDate, Date endDate, boolean isAuthor, String searchString, FilterType filterType, int page);
    ResponseEntity<?> editMemberInfo(UpdateUserInfo userInfo);
    void deleteMemberByEmail(String[] email);
    boolean isDuplicateName(String name);
}
