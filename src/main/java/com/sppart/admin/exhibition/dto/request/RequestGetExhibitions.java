package com.sppart.admin.exhibition.dto.request;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
public class RequestGetExhibitions {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String title;
    private String artistName;

    @Builder
    public RequestGetExhibitions(LocalDate startDate, LocalDate endDate, String title, String artistName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.artistName = artistName;
    }
}
