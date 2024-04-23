package com.sppart.admin.product.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
public class PictureInfo {

    private final Long product_id;
    private final String type;
    private final String size;
    private final String year;

    @Builder
    public PictureInfo(Long product_id, String type, String size, String year) {
        this.product_id = product_id;
        this.type = type;
        this.size = size;
        this.year = year;
    }
}
