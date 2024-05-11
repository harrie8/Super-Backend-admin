package com.sppart.admin.member.service;

import com.sppart.admin.member.domain.entity.Member;
import com.sppart.admin.member.domain.mapper.MemberMapper;
import com.sppart.admin.member.dto.Members;
import com.sppart.admin.member.dto.ResponseMemberDetail;
import com.sppart.admin.member.dto.ResponseMembers;
import com.sppart.admin.member.dto.UpdateUserInfo;
import com.sppart.admin.objectstorage.service.ObjectStorageService;
import com.sppart.admin.utils.FilterType;
import com.sppart.admin.utils.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberMapper memberMapper;
    private final ObjectStorageService objectStorageService;

    @Override
    @Transactional
    public ResponseMembers getAllMembers(Date startDate, Date endDate,
                                         boolean isAuthor,
                                         String searchString,
                                         FilterType filterType,
                                         int page) {
        //Get Member Info
        List<Members> members =
                getMemberListByFilter(filterType, searchString, page, startDate, endDate, isAuthor, true);

        return ResponseMembers.builder()
                .members(members)
                .total(members.size()).build();
    }

    @Override
    public ResponseMemberDetail getMemberByEmail(String email) {
        return new ResponseMemberDetail(memberMapper.getMemberByEmail(email));
    }

    @Override
    @Transactional
    public ResponseMembers getDeleteMembers(Date startDate, Date endDate,
                                              boolean isAuthor,
                                              String searchString,
                                              FilterType filterType,
                                              int page) {
        //Get Member Info
        List<Members> members =
                getMemberListByFilter(filterType, searchString, page, startDate, endDate, isAuthor, false);

        return ResponseMembers.builder()
                .members(members)
                .total(members.size()).build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> editMemberInfo(UpdateUserInfo userInfo) {
        String userEmail = userInfo.getEmail();
        if (Objects.nonNull(userInfo.getNickname())) {
            memberMapper.updateUserNicknameByEmail(userEmail, userInfo.getNickname());
        }

        if (Objects.nonNull(userInfo.getGender())) {
            memberMapper.updateUserGenderByEmail(userEmail, userInfo.getGender());
        }

        if (Objects.nonNull(userInfo.getBirth())) {
            memberMapper.updateUserBirthByEmail(userEmail, userInfo.getBirth());
        }

        if (Objects.nonNull(userInfo.getImage())) {
            String profileName = objectStorageService.uploadFile(userInfo.getImage());
            memberMapper.updateUserProfileByEmail(userEmail, profileName);
        }

        return ResponseEntity.ok("");
    }

    @Override
    @Transactional
    public void deleteMemberByEmail(String[] email) {
        Arrays.stream(email).forEach(memberMapper::deleteMemberByEmail);
    }

    @Override
    public boolean isDuplicateName(String name) {
        return memberMapper.isDuplicateName(name);
    }

    @Transactional
    public List<Members> getMemberListByFilter(FilterType filterType,
                                          String searchString,
                                          int page,
                                          Date startDate, Date endDate,
                                          boolean isAuthor, boolean isActiveMember){
        int[] pageIndex = Page.getPageIndex(page);

        switch (filterType){
            case all -> {
                if (Objects.nonNull(startDate)) {
                    if (isAuthor) return getArtistMembersByDate(startDate, endDate, pageIndex, isActiveMember);
                    else return getMembersByDate(startDate, endDate, pageIndex, isActiveMember);
                } else {
                    if (isAuthor) return getArtistMembers(pageIndex, isActiveMember);
                    else return entityToResponse(memberMapper.getAllMembers(pageIndex[0], pageIndex[1], isActiveMember));
                }
            }

            case email -> {
                if (Objects.nonNull(startDate)) {
                    if (isAuthor) return getArtistMembersByDateAndEmail(searchString, startDate, endDate, pageIndex, isActiveMember);
                    else return getMembersByDateAndEmail(searchString, startDate, endDate, pageIndex, isActiveMember);
                } else {
                    if (isAuthor) return getArtistMembersByEmail(searchString, pageIndex, isActiveMember);
                    else return entityToResponse(memberMapper.getMembersByEmail(searchString, pageIndex[0], pageIndex[1], isActiveMember));
                }
            }

            case name -> {
                if (Objects.nonNull(startDate)) {
                    if (isAuthor) return getArtistMembersByDateAndName(searchString, startDate, endDate, pageIndex, isActiveMember);
                    else return getMembersByDateAndName(searchString, startDate, endDate, pageIndex, isActiveMember);
                } else {
                    if (isAuthor) return getArtistMembersByName(searchString, pageIndex, isActiveMember);
                    else return entityToResponse(memberMapper.getMembersByName(searchString, pageIndex[0], pageIndex[1], isActiveMember));
                }
            }

            default -> {
                log.error("[{}] Parsing Error.. Wrong Filter Type.", getClass().getSimpleName());
                throw new RuntimeException();
            }
        }
    }

    @Transactional
    public List<Members> getArtistMembers(int[] pageIndex, boolean isActiveMember){
        return entityToResponse(memberMapper.getArtisMember(pageIndex[0], pageIndex[1], isActiveMember));
    }

    @Transactional
    public List<Members> getArtistMembersByEmail(String searchString, int[] pageIndex, boolean isActiveMember){
        return entityToResponse(memberMapper.getArtistMembersByEmail(searchString, pageIndex[0], pageIndex[1], isActiveMember));
    }

    @Transactional
    public List<Members> getArtistMembersByName(String searchString, int[] pageIndex, boolean isActiveMember){
        return entityToResponse(memberMapper.getArtistMembersByName(searchString, pageIndex[0], pageIndex[1], isActiveMember));
    }

    @Transactional
    public List<Members> getMembersByDate(Date startDate, Date endDate, int[] pageIndex, boolean isActiveMember) {
        return entityToResponse(memberMapper.getMembersByDate(startDate, endDate, pageIndex[0], pageIndex[1], isActiveMember));
    }

    @Transactional
    public List<Members> getMembersByDateAndEmail(String searchString, Date startDate, Date endDate, int[] pageIndex, boolean isActiveMember) {
        return entityToResponse(memberMapper.getMembersByDateAndEmail(searchString, startDate, endDate, pageIndex[0], pageIndex[1], isActiveMember));
    }

    @Transactional
    public List<Members> getMembersByDateAndName(String searchString, Date startDate, Date endDate, int[] pageIndex, boolean isActiveMember) {
        return entityToResponse(memberMapper.getMembersByDateAndName(searchString, startDate, endDate, pageIndex[0], pageIndex[1], isActiveMember));
    }

    @Transactional
    public List<Members> getArtistMembersByDate(Date startDate, Date endDate, int[] pageIndex, boolean isActiveMember) {
        return entityToResponse(memberMapper.getArtistMembersByDate(startDate, endDate, pageIndex[0], pageIndex[1], isActiveMember));
    }

    @Transactional
    public List<Members> getArtistMembersByDateAndEmail(String searchString, Date startDate, Date endDate, int[] pageIndex, boolean isActiveMember) {
         return entityToResponse(memberMapper.getArtistMembersByDateAndEmail(searchString, startDate, endDate, pageIndex[0], pageIndex[1], isActiveMember));
    }

    @Transactional
    public List<Members> getArtistMembersByDateAndName(String searchString, Date startDate, Date endDate, int[] pageIndex, boolean isActiveMember) {
         return entityToResponse(memberMapper.getArtistMembersByDateAndName(searchString, startDate, endDate, pageIndex[0], pageIndex[1], isActiveMember));
    }

    //TODO : 비동기 동작하도록 구현 필요
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Members> entityToResponse(List<Member> members){
        List<Members> memberList = new ArrayList<>(members.size());
        members.forEach(member -> memberList.add(
                            Members.builder()
                            .num(1)
                            .email(member.getEmail())
                            .name(member.getName())
                            .isAuthor(member.isArtist())
                            .createAt(member.getCreateAt())
                            .updateAt(member.getNicknameUpdateAt()).build()));
        return memberList;
    }
}
