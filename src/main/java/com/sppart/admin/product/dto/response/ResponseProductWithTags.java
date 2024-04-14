package com.sppart.admin.product.dto.response;

import com.sppart.admin.product.dto.ProductWithTagsDto;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ResponseProductWithTags {

    private long productId;
    private String title;
    private String artistName;
    private Set<String> tags;
    private String description;
    private int price;

    @Builder
    public ResponseProductWithTags(long productId, String title, String artistName, Set<String> tags,
                                   String description, int price) {
        this.productId = productId;
        this.title = title;
        this.artistName = artistName;
        this.tags = tags;
        this.description = description;
        this.price = price;
    }

    public static ResponseProductWithTags of(ProductWithTagsDto productWithTagsDto) {
        return ResponseProductWithTags.builder()
                .productId(productWithTagsDto.getProductId())
                .title(productWithTagsDto.getTitle())
                .artistName(productWithTagsDto.getArtistName())
                .tags(productWithTagsDto.getTags())
                .description(productWithTagsDto.getDescription())
                .price(productWithTagsDto.getPrice())
                .build();
    }
}
