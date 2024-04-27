package com.sppart.admin.tag.domain.entity;

import com.sppart.admin.exception.CommonErrorCode;
import com.sppart.admin.exception.SuperpositionAdminException;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tag {

    private final long tag_id;
    private final String name;

    @Builder
    public Tag(long tag_id, String name) {
        if (name == null || name.isEmpty()) {
            throw new SuperpositionAdminException(CommonErrorCode.INVALID_PARAMETER);
        }
        this.tag_id = tag_id;
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
        return getTag_id() == tag.getTag_id();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTag_id());
    }
}
