package com.sppart.admin.artist.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ResponseArtists {
    List<?> artists;
    int total;
}
