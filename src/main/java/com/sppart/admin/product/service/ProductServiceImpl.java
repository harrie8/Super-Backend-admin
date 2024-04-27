package com.sppart.admin.product.service;

import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.objectstorage.service.ObjectStorageService;
import com.sppart.admin.pictureinfo.domain.mapper.PictureInfoMapper;
import com.sppart.admin.product.domain.entity.Product;
import com.sppart.admin.product.domain.mapper.ProductMapper;
import com.sppart.admin.product.dto.DetailProductInfo;
import com.sppart.admin.product.dto.ProductSearchCondition;
import com.sppart.admin.product.dto.request.RequestCreateProduct;
import com.sppart.admin.product.dto.response.ResponseGetProductsByCondition;
import com.sppart.admin.product.dto.response.ResponsePaging;
import com.sppart.admin.product.exception.ProductErrorCode;
import com.sppart.admin.productwithtag.domain.mapper.ProductWithTagMapper;
import com.sppart.admin.tag.domain.entity.Tags;
import com.sppart.admin.tag.domain.mapper.TagMapper;
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
    private final PictureInfoMapper pictureInfoMapper;
    private final ProductWithTagMapper productWithTagMapper;
    private final TagMapper tagMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponsePaging<ResponseGetProductsByCondition> getProductsByCondition(
            ProductSearchCondition condition) {
        int totalProductCount = productMapper.countAll();
        List<DetailProductInfo> findDetailProductInfos = productMapper.findDetailProductInfosByCondition(condition);

        return toResponse(totalProductCount, findDetailProductInfos);
    }

    private ResponsePaging<ResponseGetProductsByCondition> toResponse(int totalCount,
                                                                      List<DetailProductInfo> detailProductInfos) {
        List<ResponseGetProductsByCondition> responseDetailProductInfos = detailProductInfos.stream()
                .map(ResponseGetProductsByCondition::of)
                .toList();

        return ResponsePaging.<ResponseGetProductsByCondition>builder()
                .totalCount(totalCount)
                .findResultCount(responseDetailProductInfos.size())
                .findDomains(responseDetailProductInfos)
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

    // todo 전시 이력 조회 기능 구현
    @Override
    @Transactional(readOnly = true)
    public DetailProductInfo getDetailInfoById(Long productId) {
        return productMapper.findDetailProductInfoById(productId)
                .orElseThrow(() -> new SuperpositionAdminException(ProductErrorCode.NOT_FOUND));
    }

    @Override
    @Transactional
    public long create(RequestCreateProduct req, MultipartFile picture) {
        req.validateTags();

//        String uuidFileName = objectStorageService.uploadFile(picture);

        Product product = Product.create("uuidFileName", req);
        productMapper.save(product);

        Long productId = product.getProduct_id();
        pictureInfoMapper.saveBy(productId, req.getPictureInfo());

        // todo cache 적용해보기
        Tags tags = new Tags(tagMapper.findByIds(req.getTagIds()));
        productWithTagMapper.saveBy(productId, tags.getIds());

        return productId;
    }
}
