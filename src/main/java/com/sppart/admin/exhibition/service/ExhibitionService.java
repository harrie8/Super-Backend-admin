package com.sppart.admin.exhibition.service;

import com.sppart.admin.exhibition.dto.ExhibitionSearchCondition;
import com.sppart.admin.exhibition.dto.RequestUpdateExhibitionDisplay;
import com.sppart.admin.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.exhibition.dto.ResponseGetExhibitionsByCondition;
import java.util.Set;

public interface ExhibitionService {

    ResponseGetExhibitionsByCondition getExhibitionsByCondition(ExhibitionSearchCondition condition);

    ResponseBulkDeleteByIds bulkDeleteByIds(Set<Long> ids);

    void updateOnlyDisplay(Long exhibitionId, RequestUpdateExhibitionDisplay req);
}
