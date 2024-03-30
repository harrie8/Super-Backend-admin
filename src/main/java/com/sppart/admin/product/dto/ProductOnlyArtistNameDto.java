package com.sppart.admin.product.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductOnlyArtistNameDto {
    private long productId;
    private String artistName;

    public boolean isSameArtistName(String artistName) {
        return this.artistName.equals(artistName);
    }
}