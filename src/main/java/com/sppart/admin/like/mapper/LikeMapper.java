package com.sppart.admin.like.mapper;

import com.sppart.admin.like.dto.LikeWithUser;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {

    int getTotalCountByProductId(@Param("productId") long productId);

    List<LikeWithUser> getLikesByProductId(@Param("offset") int offset, @Param("limit") int limit,
                                           @Param("productId") long productId);
}
