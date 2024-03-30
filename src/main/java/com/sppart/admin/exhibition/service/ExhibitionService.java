package com.sppart.admin.exhibition.service;

import com.sppart.admin.exhibition.dto.ExhibitionSearchCondition;
import com.sppart.admin.exhibition.dto.ResponseGetExhibitionsByCondition;

public interface ExhibitionService {

    ResponseGetExhibitionsByCondition getExhibitionsByCondition(ExhibitionSearchCondition condition);
}
