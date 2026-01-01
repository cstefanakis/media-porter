package org.sda.mediaporter.services.tvShowServices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sda.mediaporter.models.TvShow;


import java.time.LocalDateTime;

public interface TvShowService {

    TvShow updateTvShowModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime);
    TvShow getOrCreateTvShowByTitle(String tvShowTitle);

    void updateModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime);

    TvShow getTvShowById(Long id);

    Page<TvShow> getTvShows(Pageable pageable);
}
