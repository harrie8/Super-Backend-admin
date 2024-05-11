package com.sppart.admin.exhibition.service;

import com.sppart.admin.exhibition.dto.ExhibitionSearchCondition;
import com.sppart.admin.exhibition.dto.ExhibitionWithParticipatedProducts;
import com.sppart.admin.exhibition.dto.RequestUpdateExhibitionDisplay;
import com.sppart.admin.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.exhibition.dto.ResponseGetExhibitionsByCondition;
import com.sppart.admin.exhibition.dto.request.RequestCreateExhibition;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public interface ExhibitionService {

    ResponseGetExhibitionsByCondition getExhibitionsByCondition(ExhibitionSearchCondition condition);

    ResponseBulkDeleteByIds bulkDeleteByIds(Set<Long> ids);

    void updateOnlyDisplay(Long exhibitionId, RequestUpdateExhibitionDisplay req);

    ExhibitionWithParticipatedProducts getByIdWithParticipatedProducts(Long exhibitionId);

    long create(RequestCreateExhibition req, MultipartFile poster);
}
