package com.sppart.admin.exhibition.service;

import com.sppart.admin.exhibition.domain.mapper.ExhibitionMapper;
import com.sppart.admin.exhibition.dto.ExhibitionByCondition;
import com.sppart.admin.exhibition.dto.ExhibitionSearchCondition;
import com.sppart.admin.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.exhibition.dto.ResponseExhibitionByCondition;
import com.sppart.admin.exhibition.dto.ResponseGetExhibitionsByCondition;
import com.sppart.admin.product.dto.ProductOnlyArtistNameDto;
import com.sppart.admin.productexhibition.mapper.ProductExhibitionMapper;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionMapper exhibitionMapper;
    private final ProductExhibitionMapper productExhibitionMapper;

    // todo 성능 체크 해보기 -> join vs domain 별로 db 접근
    @Override
    @Transactional(readOnly = true)
    public ResponseGetExhibitionsByCondition getExhibitionsByCondition(ExhibitionSearchCondition condition) {
        int totalExhibitionCount = exhibitionMapper.countAll();
        List<ExhibitionByCondition> findExhibitions = exhibitionMapper.findExhibitionsByCondition(condition);

        if (condition.hasArtistName()) {
            return toResponse(totalExhibitionCount,
                    findExhibitionsFilteringByConditionArtistName(condition, findExhibitions));
        }

        return toResponse(totalExhibitionCount, findExhibitions);
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

    private ResponseGetExhibitionsByCondition toResponse(int totalCount,
                                                         List<ExhibitionByCondition> exhibitionByConditions) {
        List<ResponseExhibitionByCondition> findExhibitions = exhibitionByConditions.stream()
                .map(ResponseExhibitionByCondition::from)
                .toList();

        return ResponseGetExhibitionsByCondition.builder()
                .totalCount(totalCount)
                .findResultCount(findExhibitions.size())
                .findExhibitions(findExhibitions)
                .build();
    }

    @Override
    @Transactional
    public ResponseBulkDeleteByIds bulkDeleteByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseBulkDeleteByIds.zero();
        }
        int productExhibitionDeleteCount = productExhibitionMapper.bulkDeleteByExhibitionIds(ids);
        int exhibitionDeleteCount = exhibitionMapper.bulkDeleteByIds(ids);
        return ResponseBulkDeleteByIds.builder()
                .exhibitionDeleteCount(exhibitionDeleteCount)
                .productExhibitionDeleteCount(productExhibitionDeleteCount)
                .build();
    }
}
