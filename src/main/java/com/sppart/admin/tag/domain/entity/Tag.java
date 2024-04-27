package com.sppart.admin.tag.domain.entity;

import com.sppart.admin.exception.CommonErrorCode;
import com.sppart.admin.exception.SuperpositionAdminException;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Tag {

    private final long tagId;
    private final String name;

    @Builder
    public Tag(long tagId, String name) {
        if (name == null || name.isEmpty()) {
            throw new SuperpositionAdminException(CommonErrorCode.INVALID_PARAMETER);
        }
        this.tagId = tagId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return getTagId() == tag.getTagId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTagId());
    }
}
