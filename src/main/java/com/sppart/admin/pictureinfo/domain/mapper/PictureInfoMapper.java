package com.sppart.admin.pictureinfo.domain.mapper;

import com.sppart.admin.pictureinfo.dto.RequestCreatePictureInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PictureInfoMapper {

    void saveBy(@Param("productId") Long productId, @Param("req") RequestCreatePictureInfo req);
}
