package com.sppart.admin.main.product.dto;

import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.main.product.exception.ProductPageErrorCode;
import lombok.Getter;

@Getter
public class ProductPageRequest {

    private final Integer page;
    private final Integer size;

    public ProductPageRequest(Integer page, Integer size) {
        if (page != null && size == null) {
            throw new SuperpositionAdminException(ProductPageErrorCode.INVALID_PAGE_AND_SIZE_PARAMETER);
        }

        if (size != null && page == null) {
            throw new SuperpositionAdminException(ProductPageErrorCode.INVALID_PAGE_AND_SIZE_PARAMETER);
        }

        if (page != null && page < 1) {
            throw new SuperpositionAdminException(ProductPageErrorCode.INVALID_PAGE_PARAMETER);
        }

        if (size != null && size < 1) {
            throw new SuperpositionAdminException(ProductPageErrorCode.INVALID_SIZE_PARAMETER);
        }

        this.page = page;
        this.size = size;
    }

    public int getOffset() {
        if (page == null || size == null) {
            throw new SuperpositionAdminException(ProductPageErrorCode.INVALID_STATUS);
        }
        return (page - 1) * size;
    }

    public boolean isValidPaging() {
        return page != null && size != null;
    }

    public boolean isNotValidPaging() {
        return page == null || size == null;
    }
}
