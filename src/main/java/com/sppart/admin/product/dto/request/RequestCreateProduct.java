package com.sppart.admin.product.dto.request;

import com.sppart.admin.exception.CommonErrorCode;
import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.pictureinfo.dto.RequestCreatePictureInfo;
import com.sppart.admin.productwithtag.dto.RequestCreateTag;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "작품 생성 요청값")
@Getter
@NoArgsConstructor
public class RequestCreateProduct {

    @Schema(description = "작품 명 - 필수", example = "roses", required = true)
    @NotBlank
    private String title;
    @Schema(description = "작가 명 - 필수", example = "문소", required = true)
    @NotBlank
    private String artistName;
    @Schema(description = "작품 설명 - 필수", example = "나를 위로해주는 아름다운 장미, 그리고 음악과 함께 떠오르는 아련한 기억", required = true)
    @NotBlank
    private String description;
    @Valid
    private RequestCreatePictureInfo pictureInfo;
    @Schema(description = "작품 태그들 - 필수", type = "java.util.Set", required = true)
    @NotNull
    private Set<RequestCreateTag> tags;
    @Schema(description = "가격 - 필수", example = "250000", required = true)
    @NotNull
    private int price;

    @Builder
    public RequestCreateProduct(String title, String artistName, String description,
                                RequestCreatePictureInfo pictureInfo, Set<RequestCreateTag> tags, int price) {
        this.title = title;
        this.artistName = artistName;
        this.description = description;
        this.pictureInfo = pictureInfo;
        this.tags = tags;
        this.price = price;
    }

    public void validateTags() {
        if (tags.isEmpty()) {
            throw new SuperpositionAdminException(CommonErrorCode.INVALID_PARAMETER);
        }
        Integer tagLengthSum = tags.stream()
                .map(RequestCreateTag::getValue)
                .map(String::length)
                .reduce(0, Integer::sum);
        if (tagLengthSum > 11) {
            throw new SuperpositionAdminException(CommonErrorCode.INVALID_PARAMETER);
        }
    }

    public Set<Long> getTagIds() {
        return tags.stream()
                .map(RequestCreateTag::getTagId)
                .collect(Collectors.toSet());
    }
}
