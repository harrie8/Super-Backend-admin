package com.sppart.admin.product.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class DetailProductInfo {

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
    // todo 전시 관련 값 추가하기
//    private Set<String> participatedExhibitionTitles;

    @Builder
    public DetailProductInfo(Long productId, String picture, String title, String artistName, String description,
                             Integer price, Integer basicView, Integer qrView, Integer likeCount, Integer orderCount,
                             Set<String> tags) {

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
        this.tags = tags;
    }
}
