package com.sppart.admin.artist.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Builder
public class RequestArtistInfo {
    private String id;
    private String name;
    private String message;
    private String introduce;
    private MultipartFile image;
    private Date collaborationDate;
}
