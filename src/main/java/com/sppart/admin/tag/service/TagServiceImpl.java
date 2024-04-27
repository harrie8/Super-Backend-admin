package com.sppart.admin.tag.service;

import com.sppart.admin.tag.domain.entity.Tag;
import com.sppart.admin.tag.domain.mapper.TagMapper;
import com.sppart.admin.tag.dto.response.ResponseAllTags;
import com.sppart.admin.tag.dto.response.ResponseTag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponseAllTags getAllTags() {
        List<Tag> tags = tagMapper.findAll();

        List<ResponseTag> responseTags = tags.stream()
                .map(ResponseTag::from)
                .toList();
        return new ResponseAllTags(responseTags);
    }
}
