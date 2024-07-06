package com.sppart.admin.main.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductSearchCondition {

    private ProductPageRequest productPageRequest;
    private String title;
    private String artistName;
    private Long productId; // 작품 코드

    @Builder
    public ProductSearchCondition(ProductPageRequest productPageRequest, String title, String artistName,
                                  Long productId) {
        this.productPageRequest = productPageRequest;
        this.title = title;
        this.artistName = artistName;
        this.productId = productId;
    }

    public int getPage() {
        return productPageRequest.getPage();
    }

    public int getSize() {
        return productPageRequest.getSize();
    }

    public boolean isNotValidPaging() {
        return productPageRequest.isNotValidPaging();
    }

    // for mybatis
    public boolean isValidPaging() {
        return productPageRequest.isValidPaging();
    }

    // for mybatis
    public int getOffset() {
        return productPageRequest.getOffset();
    }
}
