package com.sppart.admin.member.controller;

import com.sppart.admin.member.service.MemberService;
import com.sppart.admin.utils.FilterType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequiredArgsConstructor
@RestController("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getAllMembers(@RequestParam(value = "startDate", required = false) Date startDate,
                                           @RequestParam(value = "endDate", required = false) Date endDate,
                                           @RequestParam(value = "isAuthor", defaultValue = "false") boolean isAuthor,
                                           @RequestParam(value = "searchString", required = false) String searchString,
                                           @RequestParam(value = "filterType", defaultValue = "all") FilterType filterType,
                                           @RequestParam(value = "page", defaultValue = "1") int page){
        return memberService.getAllMembers(startDate, endDate, isAuthor, searchString, filterType, page);
    }
}
