package com.sppart.admin.exhibition.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@ApiModel(description = "Schemas 탭에 `RequestCreateExhibition` 참고하시면 됩니다.")
@Getter
@NoArgsConstructor
public class RequestCreateExhibition {

    @ApiModelProperty(value = "전시 제목 - 필수", example = "제목입니다.", required = true)
    @NotBlank
    private String title;
    @ApiModelProperty(value = "전시 부제목 - 필수", example = "부제목입니다.", required = true)
    @NotBlank
    private String subHeading;
    @ApiModelProperty(value = "시작일 - 필수", required = true)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @ApiModelProperty(value = "종료일 - 필수", required = true)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @ApiModelProperty(value = "전시 장소 - 필수", example = "장소", required = true)
    @NotBlank
    private String location;
    @ApiModelProperty(value = "전시 상태 - 필수", example = "current", required = true)
    @NotNull
    private String status;
    @ApiModelProperty(value = "전시에 참여할 작품 번호들", example = "[1,2,3]")
    private Set<Long> productIds;

    @Builder
    public RequestCreateExhibition(String title, String subHeading, LocalDate startDate, LocalDate endDate,
                                   String location, String status, Set<Long> productIds) {
        this.title = title;
        this.subHeading = subHeading;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.status = status;
        this.productIds = productIds;
    }
}
