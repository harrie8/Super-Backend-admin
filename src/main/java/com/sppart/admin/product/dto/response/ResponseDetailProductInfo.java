package com.sppart.admin.product.dto.response;

import com.sppart.admin.pictureinfo.dto.ResponsePictureInfo;
import com.sppart.admin.product.dto.DetailProductInfo;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ResponseDetailProductInfo {

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
    private ResponsePictureInfo pictureInfo;
    private Set<String> tags;

    @Builder
    public ResponseDetailProductInfo(Long productId, String picture, String title, String artistName,
                                     String description, Integer price, Integer basicView, Integer qrView,
                                     Integer likeCount, Integer orderCount, ResponsePictureInfo pictureInfo,
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
        this.pictureInfo = pictureInfo;
        this.tags = tags;
    }

    public static ResponseDetailProductInfo from(final DetailProductInfo product) {
        return ResponseDetailProductInfo.builder()
                .productId(product.getProductId())
                .picture(product.getPicture())
                .title(product.getTitle())
                .artistName(product.getArtistName())
                .description(product.getDescription())
                .price(product.getPrice())
                .basicView(product.getBasicView())
                .qrView(product.getQrView())
                .likeCount(product.getLikeCount())
                .orderCount(product.getOrderCount())
                .pictureInfo(ResponsePictureInfo.from(product.getPictureInfo()))
                .tags(product.getTags())
                .build();
    }
}
