package com.sppart.admin.exhibition.controller;

import com.sppart.admin.exhibition.dto.ExhibitionSearchCondition;
import com.sppart.admin.exhibition.dto.ExhibitionWithParticipatedProducts;
import com.sppart.admin.exhibition.dto.RequestUpdateExhibitionDisplay;
import com.sppart.admin.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.exhibition.dto.ResponseGetExhibitionsByCondition;
import com.sppart.admin.exhibition.dto.request.RequestCreateExhibition;
import com.sppart.admin.exhibition.dto.request.RequestGetExhibitions;
import com.sppart.admin.exhibition.dto.response.ResponseExhibitionWithParticipatedProducts;
import com.sppart.admin.exhibition.service.ExhibitionService;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibitions")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseGetExhibitionsByCondition getExhibitionsByCondition(@ModelAttribute RequestGetExhibitions req) {
        ExhibitionSearchCondition condition = ExhibitionSearchCondition.builder()
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .title(req.getTitle())
                .artistName(req.getArtistName())
                .build();

        return exhibitionService.getExhibitionsByCondition(condition);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> bulkDeleteByIds(@RequestParam(defaultValue = "") Set<Long> ids) {
        ResponseBulkDeleteByIds deleteCount = exhibitionService.bulkDeleteByIds(ids);

        return ResponseEntity.ok(deleteCount.toString());
    }

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

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void createExhibition(@Valid @RequestPart RequestCreateExhibition req, @RequestPart MultipartFile poster) {
        exhibitionService.create(req, poster);
    }
}
