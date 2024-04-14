package com.sppart.admin.product.domain.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Product {

    private final Long productId;
    private final String picture;
    private final String title;
    private final String artistName;
    private final String description;
    private final int price;
    private final int basicView;
    private final int qrView;
    private final int likeCount;
    private final int orderCount;

    @Builder
    public Product(Long productId, String picture, String title, String artistName, String description, int price,
                   int basicView, int qrView, int likeCount, int orderCount) {
        this.productId = productId;
        this.picture = picture;
        this.title = title;
        this.artistName = artistName;
        this.description = description;
        this.price = price;
        this.basicView = basicView;
        this.qrView = qrView;
        this.likeCount = likeCount;
        this.orderCount = orderCount;
    }
}
