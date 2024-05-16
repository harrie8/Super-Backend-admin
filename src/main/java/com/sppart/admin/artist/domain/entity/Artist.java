package com.sppart.admin.artist.domain.entity;

import com.sppart.admin.artist.dto.RequestArtistInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Builder
public class Artist {
    private String name;
    private String profile;
    private String introduce;
    private String description;
    private String instagramId;
    private boolean isDisplay;
    private int view;
    private boolean isAbout;
    private Date collaborationDate;
}
