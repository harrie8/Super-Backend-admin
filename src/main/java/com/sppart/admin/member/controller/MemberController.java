package com.sppart.admin.member.controller;

import com.sppart.admin.member.dto.UpdateUserInfo;
import com.sppart.admin.member.service.MemberService;
import com.sppart.admin.utils.FilterType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        return ResponseEntity.ok(memberService.getAllMembers(startDate, endDate, isAuthor, searchString, filterType, page));
    }

    @GetMapping("/inactive")
    public ResponseEntity<?> getAllDeleteMembers(@RequestParam(value = "startDate", required = false) Date startDate,
                                           @RequestParam(value = "endDate", required = false) Date endDate,
                                           @RequestParam(value = "isAuthor", defaultValue = "false") boolean isAuthor,
                                           @RequestParam(value = "searchString", required = false) String searchString,
                                           @RequestParam(value = "filterType", defaultValue = "all") FilterType filterType,
                                           @RequestParam(value = "page", defaultValue = "1") int page){
        return ResponseEntity.ok(memberService.getDeleteMembers(startDate, endDate, isAuthor, searchString, filterType, page));
    }

    @GetMapping("/one")
    public ResponseEntity<?> getMemberByEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

    @PutMapping("/one")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> editMemberInfo(@RequestParam UpdateUserInfo userInfo) {
        return memberService.editMemberInfo(userInfo);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteMemberByEmail(@RequestParam(value = "email") String[] email) {
        memberService.deleteMemberByEmail(email);
    }

    @GetMapping("/check-name")
    public ResponseEntity<?> checkDuplicateName(@RequestParam("name") String name) {
        if (memberService.isDuplicateName(name)) {
            return ResponseEntity.ok("success.");
        } else {
            return ResponseEntity.badRequest().body("failed.");
        }
    }
}
