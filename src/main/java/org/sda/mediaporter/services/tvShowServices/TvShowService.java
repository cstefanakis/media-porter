package org.sda.mediaporter.services.tvShowServices;

import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.Path;
import java.time.LocalDateTime;

public interface TvShowService {

    TvShow updateTvShowModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime);
    TvShow getOrCreateTvShowByTitle(String tvShowTitle);

    void updateModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime);

    TvShow getTvShowById(Long id);
}
