package com.sppart.admin.user.domain.mapper;

import com.sppart.admin.user.dto.ResponseUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    ResponseUserInfo getUserInfoById(@Param("id") String id);
}
