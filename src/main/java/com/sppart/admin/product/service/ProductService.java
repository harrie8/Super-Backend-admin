package com.sppart.admin.product.service;

import com.sppart.admin.exhibition.dto.ExhibitionWithParticipatedProducts;
import com.sppart.admin.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.exhibition.dto.request.RequestCreateExhibition;
import com.sppart.admin.product.dto.ProductSearchCondition;
import com.sppart.admin.product.dto.response.ResponseGetProductsWithTagsByCondition;
import com.sppart.admin.product.dto.response.ResponseProductWithTags;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ResponseGetProductsWithTagsByCondition<ResponseProductWithTags> getProductsByCondition(
            ProductSearchCondition condition);

    ResponseBulkDeleteByIds bulkDeleteByIds(Set<Long> ids);

    ExhibitionWithParticipatedProducts getByIdWithParticipatedProducts(Long exhibitionId);

    long create(RequestCreateExhibition req, MultipartFile poster);
}
