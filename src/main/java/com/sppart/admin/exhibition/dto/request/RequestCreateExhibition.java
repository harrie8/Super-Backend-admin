package com.sppart.admin.exhibition.dto.request;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class RequestCreateExhibition {

    @NotBlank
    private String title;
    @NotBlank
    private String subHeading;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotBlank
    private String location;
    @NotNull
    private String status;
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
