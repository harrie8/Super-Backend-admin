package com.sppart.admin.user.domain.mapper;

import com.sppart.admin.user.dto.ResponseUserInfo;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    Optional<ResponseUserInfo> getUserInfoById(@Param("id") String id);
}
