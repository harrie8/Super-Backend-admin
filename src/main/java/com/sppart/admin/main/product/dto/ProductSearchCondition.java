package com.sppart.admin.main.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
public class ProductSearchCondition {

    private Pageable pageable;
    private String title;
    private String artistName;
    private Long productId; // 작품 코드

    @Builder
    public ProductSearchCondition(Pageable pageable, String title, String artistName, Long productId) {
        this.pageable = pageable;
        this.title = title;
        this.artistName = artistName;
        this.productId = productId;
    }

    public int getPageNumber() {
        return pageable.getPageNumber();
    }

    public int getPageSize() {
        return pageable.getPageSize();
    }

    // for mybatis
    public int getOffset() {
        return (pageable.getPageNumber() - 1) * pageable.getPageSize();
    }
}
