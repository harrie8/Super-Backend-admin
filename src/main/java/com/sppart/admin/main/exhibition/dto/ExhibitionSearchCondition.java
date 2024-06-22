package com.sppart.admin.main.exhibition.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
public class ExhibitionSearchCondition {

    private Pageable pageable;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String artistName;

    @Builder
    public ExhibitionSearchCondition(Pageable pageable, LocalDate startDate, LocalDate endDate, String title,
                                     String artistName) {
        this.pageable = pageable;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.artistName = artistName;
    }

    public boolean hasArtistName() {
        return artistName != null && !artistName.isBlank();
    }

    public int getPageNumber() {
        return pageable.getPageNumber();
    }

    public int getPageSize() {
        return pageable.getPageSize();
    }

    // for mybatis
    public int getOffset() {
        return (pageable.getPageNumber() - 1) * pageable.getPageSize();
    }
}
