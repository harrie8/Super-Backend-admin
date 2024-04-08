package com.sppart.admin.member.domain.mapper;

import com.sppart.admin.member.domain.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<Member> getAllMembers();
}
