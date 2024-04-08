package com.sppart.admin.member.domain.mapper;

import com.sppart.admin.member.domain.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<Member> getAllMembers();

    List<Member> getMembersByEmail(@Param("email") String email);
    List<Member> getMembersByName(@Param("name") String name);
}
