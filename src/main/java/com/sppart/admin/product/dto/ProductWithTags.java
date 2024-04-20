package com.sppart.admin.product.dto;

import java.util.Set;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductWithTags {
    private Long productId;
    private String picture;
    private String title;
    private String artistName;
    private String description;
    private Integer price;
    private Integer basicView;
    private Integer qrView;
    private Integer likeCount;
    private Integer orderCount;
    private Set<String> tags;

}