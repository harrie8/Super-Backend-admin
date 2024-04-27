package com.sppart.admin.tag.domain.mapper;

import com.sppart.admin.product.domain.entity.Product;
import com.sppart.admin.tag.domain.entity.Tag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper {

    List<Tag> findAll();

    void save(Product product);
}
