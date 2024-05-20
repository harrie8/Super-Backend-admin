package com.sppart.admin.tag.domain.mapper;

import com.sppart.admin.tag.domain.entity.Tag;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DuplicateKeyException;

@Mapper
public interface TagMapper {

    List<Tag> findAll();

    List<Tag> findByIds(@Param("tagIds") Set<Long> tagIds);

    void save(Tag tag) throws DuplicateKeyException;
}
