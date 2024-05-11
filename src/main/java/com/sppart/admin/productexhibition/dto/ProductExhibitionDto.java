package com.sppart.admin.productexhibition.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductExhibitionDto {

    private long productId;
    private long exhibitionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
