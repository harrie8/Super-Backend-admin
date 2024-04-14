package com.sppart.admin.product.dto;

import java.util.Set;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductWithTagsDto {
    private long productId;
    private String title;
    private String artistName;
    private Set<String> tags;
    private String description;
    private int price;

}