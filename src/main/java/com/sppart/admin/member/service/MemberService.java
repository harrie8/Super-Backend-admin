package com.sppart.admin.member.service;

import com.sppart.admin.member.dto.ResponseMembers;
import com.sppart.admin.utils.FilterType;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface MemberService {
    ResponseEntity<?> getAllMembers(Date startDate, Date endDate, boolean isAuthor, String searchString, FilterType filterType, int page);
}
