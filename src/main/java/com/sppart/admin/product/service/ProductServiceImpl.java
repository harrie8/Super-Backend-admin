package com.sppart.admin.product.service;

import static java.util.stream.Collectors.groupingBy;

import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.exhibition.domain.entity.ExhibitionStatus;
import com.sppart.admin.objectstorage.service.ObjectStorageService;
import com.sppart.admin.pictureinfo.domain.mapper.PictureInfoMapper;
import com.sppart.admin.product.domain.entity.Product;
import com.sppart.admin.product.domain.mapper.ProductMapper;
import com.sppart.admin.product.dto.DetailProductInfo;
import com.sppart.admin.product.dto.ProductSearchCondition;
import com.sppart.admin.product.dto.request.RequestCreateProduct;
import com.sppart.admin.product.dto.response.ResponseBulkDeleteProductByIds;
import com.sppart.admin.product.dto.response.ResponseDetailProductInfo;
import com.sppart.admin.product.dto.response.ResponseGetProductsByCondition;
import com.sppart.admin.product.dto.response.ResponsePaging;
import com.sppart.admin.product.exception.ProductErrorCode;
import com.sppart.admin.productexhibition.dto.ExhibitionHistoryOfProductDto;
import com.sppart.admin.productexhibition.mapper.ProductExhibitionMapper;
import com.sppart.admin.productwithtag.domain.mapper.ProductWithTagMapper;
import com.sppart.admin.tag.domain.entity.Tags;
import com.sppart.admin.tag.domain.mapper.TagMapper;
import java.util.List;
import java.util.Map;
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
    private final ProductExhibitionMapper productExhibitionMapper;

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
    public ResponseBulkDeleteProductByIds bulkDeleteByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseBulkDeleteProductByIds.zero();
        }

        List<DetailProductInfo> findProducts = productMapper.findDetailProductInfoByIds(ids);

        deletePictures(findProducts);

        int productWithTagDeleteCount = productWithTagMapper.bulkDeleteByProductIds(ids);
        int pictureInfoDeleteCount = pictureInfoMapper.bulkDeleteByProductIds(ids);
        int productDeleteCount = productMapper.bulkDeleteByIds(ids);
        return ResponseBulkDeleteProductByIds.builder()
                .productDeleteCount(productDeleteCount)
                .productWithTagDeleteCount(productWithTagDeleteCount)
                .pictureInfoDeleteCount(pictureInfoDeleteCount)
                .build();
    }

    private void deletePictures(List<DetailProductInfo> findProducts) {
        List<String> pictureUrls = findProducts.stream()
                .map(DetailProductInfo::getPicture)
                .toList();
        objectStorageService.delete(pictureUrls);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDetailProductInfo getDetailInfoById(Long productId) {
        DetailProductInfo findDetailProductInfo = productMapper.findDetailProductInfoById(productId)
                .orElseThrow(() -> new SuperpositionAdminException(ProductErrorCode.NOT_FOUND));
        Map<ExhibitionStatus, List<ExhibitionHistoryOfProductDto>> exhibitionHistory = getExhibitionHistory(
                findDetailProductInfo);

        return ResponseDetailProductInfo.from(findDetailProductInfo, exhibitionHistory);
    }

    private Map<ExhibitionStatus, List<ExhibitionHistoryOfProductDto>> getExhibitionHistory(
            DetailProductInfo findDetailProductInfo) {
        List<ExhibitionHistoryOfProductDto> findExhibitionHistoryOfProduct = productExhibitionMapper.findExhibitionHistoryOfProductByProductId(
                findDetailProductInfo.getProductId());
        return findExhibitionHistoryOfProduct.stream()
                .collect(groupingBy(ExhibitionHistoryOfProductDto::getExhibitionStatus));
    }

    @Override
    @Transactional
    public long create(RequestCreateProduct req, MultipartFile picture) {
        req.validateTags();

        String uuidFileName = objectStorageService.uploadFile(picture);

        Product product = Product.create(uuidFileName, req);
        productMapper.save(product);

        Long productId = product.getProduct_id();
        pictureInfoMapper.saveBy(productId, req.getPictureInfo());

        // todo cache 적용해보기
        Tags tags = new Tags(tagMapper.findByIds(req.getTagIds()));
        productWithTagMapper.saveBy(productId, tags.getIds());

        return productId;
    }
}
