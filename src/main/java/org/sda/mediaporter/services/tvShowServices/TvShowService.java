package org.sda.mediaporter.services.tvShowServices;

import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.Path;
import java.time.LocalDateTime;

public interface TvShowService {

    Page<TvShow> getTvShows(Pageable pageable);
    Page<TvShow> getTvShowsBySourcePath(Pageable pageable, SourcePath sourcePath);
    Page<TvShow> getTvShowsByTitle(Pageable pageable, String title);
    TvShow getTvShowById(Long tvShowId);
    boolean deleteTvShowById(Long tvShowId);
    boolean moveTvShowById(Long tvShowId, Path destinationPath);
    TvShow updateTvShowModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime);
    TvShow getOrCreateTvShowByTitle(String tvShowTitle);
    void autoMoveTvShowsFromSourcePathToTvShowsPath();
    void autoGetAndUpdateTvShowsFromTvShowsPath();
    void autoCopyTvShowsFromSourcePathToTvShowsPath();

}
