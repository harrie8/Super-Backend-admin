package com.sppart.admin.exhibition.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestUpdateExhibitionDisplay {

    @Min(0)
    @Max(1)
    private int isDisplay;

    @Builder
    public RequestUpdateExhibitionDisplay(int isDisplay) {
        this.isDisplay = isDisplay;
    }
}
