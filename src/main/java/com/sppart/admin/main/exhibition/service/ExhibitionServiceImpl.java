package com.sppart.admin.main.exhibition.service;

import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.main.exhibition.domain.entity.Exhibition;
import com.sppart.admin.main.exhibition.domain.entity.ExhibitionStatus;
import com.sppart.admin.main.exhibition.domain.mapper.ExhibitionMapper;
import com.sppart.admin.main.exhibition.dto.ExhibitionByCondition;
import com.sppart.admin.main.exhibition.dto.ExhibitionSearchCondition;
import com.sppart.admin.main.exhibition.dto.ExhibitionWithParticipatedProducts;
import com.sppart.admin.main.exhibition.dto.RequestUpdateExhibitionDisplay;
import com.sppart.admin.main.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.main.exhibition.dto.request.RequestCreateExhibition;
import com.sppart.admin.main.exhibition.dto.request.RequestUpdateExhibition;
import com.sppart.admin.main.exhibition.exception.ExhibitionErrorCode;
import com.sppart.admin.main.product.dto.ProductOnlyArtistNameDto;
import com.sppart.admin.main.productexhibition.domain.mapper.ProductExhibitionMapper;
import com.sppart.admin.objectstorage.service.ObjectStorageService;
import com.sppart.admin.utils.PageInfo;
import com.sppart.admin.utils.PageInfo.PageInfoBuilder;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionMapper exhibitionMapper;
    private final ProductExhibitionMapper productExhibitionMapper;
    private final ObjectStorageService objectStorageService;

    @Override
    @Transactional(readOnly = true)
    public PageInfo<ExhibitionByCondition> getExhibitionsByCondition(ExhibitionSearchCondition condition) {
        int totalExhibitionCount = exhibitionMapper.countAll();
        List<ExhibitionByCondition> findExhibitions = exhibitionMapper.findExhibitionsByCondition(condition);

        PageInfoBuilder<ExhibitionByCondition> exhibitionByConditionPageInfoBuilder = PageInfo.<ExhibitionByCondition>builder()
                .pageIndex(condition.getPageNumber())
                .pageSize(condition.getPageSize())
                .totalCount(totalExhibitionCount);

        if (condition.hasArtistName()) {
            return exhibitionByConditionPageInfoBuilder
                    .data(findExhibitionsFilteringByConditionArtistName(condition, findExhibitions))
                    .build();
        }

        return exhibitionByConditionPageInfoBuilder
                .data(findExhibitions)
                .build();
    }

    private List<ExhibitionByCondition> findExhibitionsFilteringByConditionArtistName(
            ExhibitionSearchCondition condition, List<ExhibitionByCondition> findExhibitions) {
        Predicate<ProductOnlyArtistNameDto> sameArtistName = p -> p.isSameArtistName(condition.getArtistName());
        return findExhibitions.stream()
                .filter(exhibition -> {
                    List<ProductOnlyArtistNameDto> products = exhibition.getProducts();
                    return products.stream()
                            .anyMatch(sameArtistName);
                })
                .toList();
    }

    @Override
    @Transactional
    public ResponseBulkDeleteByIds bulkDeleteByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseBulkDeleteByIds.zero();
        }

        List<Exhibition> findExhibitions = exhibitionMapper.findByIds(ids);
        deletePosters(findExhibitions);

        int productExhibitionDeleteCount = productExhibitionMapper.bulkDeleteByExhibitionIds(ids);
        int exhibitionDeleteCount = exhibitionMapper.bulkDeleteByIds(ids);
        return ResponseBulkDeleteByIds.builder()
                .exhibitionDeleteCount(exhibitionDeleteCount)
                .productExhibitionDeleteCount(productExhibitionDeleteCount)
                .build();
    }

    private void deletePosters(List<Exhibition> findExhibitions) {
        if (findExhibitions.isEmpty()) {
            return;
        }
        List<String> posterUrls = findExhibitions.stream()
                .map(Exhibition::getPoster)
                .toList();
        objectStorageService.delete(posterUrls);
    }

    @Override
    @Transactional
    public String updateOnlyDisplay(Long exhibitionId, RequestUpdateExhibitionDisplay req) {
        Exhibition findExhibition = exhibitionMapper.findById(exhibitionId)
                .orElseThrow(() -> new SuperpositionAdminException(ExhibitionErrorCode.NOT_FOUND));
        exhibitionMapper.updateOnlyDisplay(exhibitionId, req.getIsDisplay());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("현재 exhibition display : ").append(findExhibition.getIsDisplay()).append("\n");
        Exhibition updateExhibition = exhibitionMapper.findById(exhibitionId)
                .orElseThrow(() -> new SuperpositionAdminException(ExhibitionErrorCode.NOT_FOUND));
        stringBuilder.append("바뀐 exhibition display : ").append(updateExhibition.getIsDisplay()).append("\n");

        return stringBuilder.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public ExhibitionWithParticipatedProducts getByIdWithParticipatedProducts(Long exhibitionId) {
        ExhibitionWithParticipatedProducts exhibitionWithParticipatedProducts = exhibitionMapper.findByIdWithParticipatedProducts(
                exhibitionId).orElseThrow(() -> new SuperpositionAdminException(ExhibitionErrorCode.NOT_FOUND));
        exhibitionWithParticipatedProducts.sortProductsByProductId();
        return exhibitionWithParticipatedProducts;
    }

    @Override
    @Transactional
    public long create(RequestCreateExhibition req, MultipartFile poster) {
        String url = objectStorageService.uploadFile(poster);

        Exhibition exhibition = Exhibition.builder()
                .title(req.getTitle())
                .subHeading(req.getSubHeading())
                .location(req.getLocation())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .status(ExhibitionStatus.findByName(req.getStatus()))
                .poster(url)
                .isDisplay(0)
                .build();
        exhibitionMapper.save(exhibition);

        productExhibitionMapper.bulkInsertByExhibitionId(exhibition.getId(), req.getProductIds());

        return exhibition.getId();
    }

    @Override
    public void update(Long exhibitionId, RequestUpdateExhibition req, MultipartFile poster) {
        String posterURL = getPosterURL(req, poster);

        exhibitionMapper.update(exhibitionId, posterURL, req);

        productExhibitionMapper.deleteByExhibitionId(exhibitionId);
        productExhibitionMapper.bulkInsertByExhibitionId(exhibitionId, req.getProductIds());
    }

    private String getPosterURL(RequestUpdateExhibition req, MultipartFile poster) {
        if (poster != null && !poster.isEmpty()) {
            objectStorageService.delete(req.getOldPoster());
            return objectStorageService.uploadFile(poster);
        }
        return req.getOldPoster();
    }
}
