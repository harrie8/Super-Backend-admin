package com.sppart.admin.productexhibition.dto;

import com.sppart.admin.exhibition.domain.entity.ExhibitionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionHistoryOfProductDto {

    private long exhibitionId;
    private String exhibitionTitle;
    private ExhibitionStatus exhibitionStatus;
}
