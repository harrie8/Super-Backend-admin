package com.sppart.admin.product.dto;

import com.sppart.admin.pictureinfo.domain.entity.PictureInfo;
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
    private PictureInfo pictureInfo;
    private int price;

}