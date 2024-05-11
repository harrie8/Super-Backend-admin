package com.sppart.admin.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResponseMembers {
    private List<?> members;
    private int total;
}
