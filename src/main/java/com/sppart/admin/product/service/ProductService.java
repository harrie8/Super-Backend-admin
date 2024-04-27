package com.sppart.admin.product.service;

import com.sppart.admin.exhibition.dto.ResponseBulkDeleteByIds;
import com.sppart.admin.product.dto.DetailProductInfo;
import com.sppart.admin.product.dto.ProductSearchCondition;
import com.sppart.admin.product.dto.request.RequestCreateProduct;
import com.sppart.admin.product.dto.response.ResponseGetProductsByCondition;
import com.sppart.admin.product.dto.response.ResponsePaging;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ResponsePaging<ResponseGetProductsByCondition> getProductsByCondition(ProductSearchCondition condition);

    ResponseBulkDeleteByIds bulkDeleteByIds(Set<Long> ids);

    DetailProductInfo getDetailInfoById(Long productId);

    long create(RequestCreateProduct req, MultipartFile picture);
}
