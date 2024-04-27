package com.sppart.admin.tag.dto.response;

import com.sppart.admin.tag.domain.entity.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseTag {

    private Long tagId;
    private String name;

    @Builder
    public ResponseTag(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    public static ResponseTag from(Tag tag) {
        return ResponseTag.builder()
                .tagId(tag.getTagId())
                .name(tag.getName())
                .build();
    }
}
