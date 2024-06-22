package com.sppart.admin.main.exhibition.service;

import com.sppart.admin.main.exhibition.dto.ExhibitionByCondition;
import com.sppart.admin.main.exhibition.dto.ExhibitionSearchCondition;
import com.sppart.admin.main.exhibition.dto.ExhibitionWithParticipatedProducts;
import com.sppart.admin.main.exhibition.dto.RequestUpdateExhibitionDisplay;
import com.sppart.admin.main.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.main.exhibition.dto.request.RequestCreateExhibition;
import com.sppart.admin.main.exhibition.dto.request.RequestUpdateExhibition;
import com.sppart.admin.utils.PageInfo;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public interface ExhibitionService {

    PageInfo<ExhibitionByCondition> getExhibitionsByCondition(ExhibitionSearchCondition condition);

    ResponseBulkDeleteByIds bulkDeleteByIds(Set<Long> ids);

    String updateOnlyDisplay(Long exhibitionId, RequestUpdateExhibitionDisplay req);

    ExhibitionWithParticipatedProducts getByIdWithParticipatedProducts(Long exhibitionId);

    long create(RequestCreateExhibition req, MultipartFile poster);

    void update(Long exhibitionId, RequestUpdateExhibition req, MultipartFile poster);
}
