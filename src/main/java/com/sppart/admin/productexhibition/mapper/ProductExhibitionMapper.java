package com.sppart.admin.productexhibition.mapper;

import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductExhibitionMapper {
    int bulkDeleteByExhibitionIds(@Param("exhibitionIds") Set<Long> exhibitionIds);

    int countAll();
}
