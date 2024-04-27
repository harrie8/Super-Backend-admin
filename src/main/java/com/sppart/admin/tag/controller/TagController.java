package com.sppart.admin.tag.controller;

import com.sppart.admin.tag.dto.response.ResponseAllTags;
import com.sppart.admin.tag.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "태그")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @ApiOperation(value = "모든 태그 조회", notes = "tagId와 tagName을 응답값으로 반환하는 API입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공", response = String.class),
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseAllTags getAllTags() {
        return tagService.getAllTags();
    }
}
