package com.sppart.admin.tag.domain.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Tags {

    private final Set<Tag> tags;

    @Builder
    public Tags(List<Tag> tags) {
        this.tags = new HashSet<>(tags);
    }

    public List<Long> getIds() {
        return tags.stream()
                .map(Tag::getTagId)
                .toList();
    }
}
