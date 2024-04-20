package com.sppart.admin.product.service;

import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.exhibition.dto.request.RequestCreateExhibition;
import com.sppart.admin.objectstorage.service.ObjectStorageService;
import com.sppart.admin.product.domain.mapper.ProductMapper;
import com.sppart.admin.product.dto.DetailProductInfo;
import com.sppart.admin.product.dto.ProductSearchCondition;
import com.sppart.admin.product.dto.ProductWithTagsDto;
import com.sppart.admin.product.dto.response.ResponseGetProductsWithTagsByCondition;
import com.sppart.admin.product.dto.response.ResponseProductWithTags;
import com.sppart.admin.product.exception.ProductErrorCode;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ObjectStorageService objectStorageService;

    @Override
    @Transactional(readOnly = true)
    public ResponseGetProductsWithTagsByCondition<ResponseProductWithTags> getProductsByCondition(
            ProductSearchCondition condition) {
        int totalProductCount = productMapper.countAll();
        List<ProductWithTagsDto> findProductsWithTags = productMapper.findProductsWithTagsByCondition(condition);

        return toResponse(totalProductCount, findProductsWithTags);
    }

    private ResponseGetProductsWithTagsByCondition<ResponseProductWithTags> toResponse(int totalCount,
                                                                                       List<ProductWithTagsDto> products) {
        List<ResponseProductWithTags> responseProductWithTags = products.stream()
                .map(ResponseProductWithTags::of)
                .toList();

        return ResponseGetProductsWithTagsByCondition.<ResponseProductWithTags>builder()
                .totalCount(totalCount)
                .findResultCount(products.size())
                .findDomains(responseProductWithTags)
                .build();
    }

    @Override
    @Transactional
    public ResponseBulkDeleteByIds bulkDeleteByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseBulkDeleteByIds.zero();
        }
        productMapper.bulkDeleteProductWithTagByProductIds(ids);
        int productDeleteCount = productMapper.bulkDeleteByIds(ids);
        return ResponseBulkDeleteByIds.builder()
                .exhibitionDeleteCount(productDeleteCount)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DetailProductInfo getDetailInfoById(Long productId) {
        return productMapper.findDetailProductInfoById(productId)
                .orElseThrow(() -> new SuperpositionAdminException(ProductErrorCode.NOT_FOUND));
    }

    @Override
    @Transactional
    public long create(RequestCreateExhibition req, MultipartFile poster) {
//        String url = objectStorageService.uploadFile(poster);
//
//        Exhibition exhibition = Exhibition.builder()
//                .title(req.getTitle())
//                .subHeading(req.getSubHeading())
//                .location(req.getLocation())
//                .startDate(req.getStartDate())
//                .endDate(req.getEndDate())
//                .status(ExhibitionStatus.findByName(req.getStatus()))
//                .poster(url)
//                .isDisplay(0)
//                .build();
//        productMapper.save(exhibition);
//
//        return exhibition.getId();
        return 1L;
    }
}
