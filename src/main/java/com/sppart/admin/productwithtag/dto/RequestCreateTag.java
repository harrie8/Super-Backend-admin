package com.sppart.admin.productwithtag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "작품의 태그들 생성 요청값")
@Getter
@NoArgsConstructor
public class RequestCreateTag {

    @Schema(description = "태그 ID", example = "1", required = true)
    private long tagId;

    @Schema(description = "태그 이름", example = "청량한", required = true)
    private String value;

    @Builder
    public RequestCreateTag(long tagId, String value) {
        this.tagId = tagId;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestCreateTag that = (RequestCreateTag) o;
        return getTagId() == that.getTagId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTagId());
    }
}
