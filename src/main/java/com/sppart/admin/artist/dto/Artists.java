package com.sppart.admin.artist.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class Artists {
    private String instagramId;
    private String name;
    private boolean isUser;
    private Date collaborationDate;
}
