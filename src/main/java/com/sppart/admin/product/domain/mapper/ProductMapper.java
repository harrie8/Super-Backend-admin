package com.sppart.admin.product.domain.mapper;

import com.sppart.admin.exhibition.dto.ExhibitionWithParticipatedProducts;
import com.sppart.admin.product.domain.entity.Product;
import com.sppart.admin.product.dto.ProductSearchCondition;
import com.sppart.admin.product.dto.ProductWithTagsDto;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

    List<ProductWithTagsDto> findProductsWithTagsByCondition(@Param("condition") ProductSearchCondition condition);

    int countAll();

    void bulkDeleteProductWithTagByProductIds(@Param("ids") Set<Long> ids);

    int bulkDeleteByIds(@Param("ids") Set<Long> ids);

    Optional<Product> findById(@Param("exhibitionId") Long exhibitionId);

    Optional<ExhibitionWithParticipatedProducts> findByIdWithParticipatedProducts(
            @Param("exhibitionId") Long exhibitionId);

    void save(Product product);
}
