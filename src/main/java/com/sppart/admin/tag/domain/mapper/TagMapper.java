package com.sppart.admin.tag.domain.mapper;

import com.sppart.admin.product.domain.entity.Product;
import com.sppart.admin.tag.domain.entity.Tag;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TagMapper {

    List<Tag> findAll();

    List<Tag> findByIds(@Param("tagIds") Set<Long> tagIds);

    void save(Product product);
}
