package com.sppart.admin.exhibition.controller;

import com.sppart.admin.exhibition.dto.ExhibitionSearchCondition;
import com.sppart.admin.exhibition.dto.ExhibitionWithParticipatedProducts;
import com.sppart.admin.exhibition.dto.RequestUpdateExhibitionDisplay;
import com.sppart.admin.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.exhibition.dto.ResponseGetExhibitionsByCondition;
import com.sppart.admin.exhibition.dto.response.ResponseExhibitionWithParticipatedProducts;
import com.sppart.admin.exhibition.service.ExhibitionService;
import java.time.LocalDate;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibitions")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseGetExhibitionsByCondition getExhibitionsByCondition(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artistName) {

        ExhibitionSearchCondition condition = ExhibitionSearchCondition.builder()
                .startDate(startDate)
                .endDate(endDate)
                .title(title)
                .artistName(artistName)
                .build();

        return exhibitionService.getExhibitionsByCondition(condition);
    }

    // todo 권한 설정하기
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> bulkDeleteByIds(@RequestParam(defaultValue = "") Set<Long> ids) {
        ResponseBulkDeleteByIds deleteCount = exhibitionService.bulkDeleteByIds(ids);

        return ResponseEntity.ok(deleteCount.toString());
    }

    // todo 권한 설정하기
    @PatchMapping("/{exhibitionId}")
    @ResponseStatus(HttpStatus.OK)
    public String updateDisplay(@PathVariable Long exhibitionId,
                                @Valid @RequestBody RequestUpdateExhibitionDisplay req) {
        exhibitionService.updateOnlyDisplay(exhibitionId, req);

        return "display update success";
    }

    @GetMapping("/{exhibitionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseExhibitionWithParticipatedProducts getByIdWithParticipatedProducts(@PathVariable Long exhibitionId) {
        ExhibitionWithParticipatedProducts result = exhibitionService.getByIdWithParticipatedProducts(
                exhibitionId);
        return ResponseExhibitionWithParticipatedProducts.from(result);
    }
}
