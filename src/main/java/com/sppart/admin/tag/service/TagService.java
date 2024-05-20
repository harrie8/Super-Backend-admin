package com.sppart.admin.tag.service;

import com.sppart.admin.tag.dto.request.RequestCreateTag;
import com.sppart.admin.tag.dto.response.ResponseAllTags;

public interface TagService {

    ResponseAllTags getAllTags();

    void create(RequestCreateTag req);
}
