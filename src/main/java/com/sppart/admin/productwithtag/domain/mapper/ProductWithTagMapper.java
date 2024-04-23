package com.sppart.admin.productwithtag.domain.mapper;

import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductWithTagMapper {

    void saveBy(@Param("productId") Long productId, @Param("tagIds") Set<Long> tagIds);
}
