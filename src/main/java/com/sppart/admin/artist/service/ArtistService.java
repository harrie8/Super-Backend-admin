package com.sppart.admin.artist.service;

import com.sppart.admin.artist.dto.RequestArtistInfo;
import com.sppart.admin.artist.dto.RequestUpdateArtistInfo;
import com.sppart.admin.artist.dto.ResponseArtistDetail;
import com.sppart.admin.artist.dto.ResponseArtists;
import com.sppart.admin.utils.FilterType;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface ArtistService {
    ResponseArtists getAllArtists(Date startDate, Date endDate, boolean isUser, String searchString, FilterType filterType, int page);
    ResponseArtistDetail getArtist(String instagramId);
    ResponseEntity<?> editArtistInfo(RequestUpdateArtistInfo artistInfo);
    ResponseEntity<?> registerArtist(RequestArtistInfo artistInfo);
    void deleteArtists(String[] ids);

}
