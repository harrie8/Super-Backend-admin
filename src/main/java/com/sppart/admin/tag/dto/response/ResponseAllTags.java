package com.sppart.admin.tag.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseAllTags {

    private List<ResponseTag> tags;

    @Builder
    public ResponseAllTags(List<ResponseTag> tags) {
        this.tags = tags;
    }
}
