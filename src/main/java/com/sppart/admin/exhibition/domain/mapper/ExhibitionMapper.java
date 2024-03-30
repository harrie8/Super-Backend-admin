package com.sppart.admin.exhibition.domain.mapper;

import com.sppart.admin.exhibition.dto.ExhibitionByCondition;
import com.sppart.admin.exhibition.dto.ExhibitionSearchCondition;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExhibitionMapper {

    List<ExhibitionByCondition> findExhibitionsByCondition(@Param("condition") ExhibitionSearchCondition condition);

    int countAll();

    int bulkDeleteByIds(@Param("ids") Set<Long> ids);
}
