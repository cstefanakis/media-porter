package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public interface TvShowService {

    Page<TvShow> getTvShows(Pageable pageable);
    Page<TvShow> getTvShowsBySourcePath(Pageable pageable, Path sourcePath);
    List<TvShow> getTvShowsByTitle(String title);
    TvShow getTvShowById(Long tvShowId);
    boolean deleteTvShowById(Long tvShowId);
    boolean moveTvShowById(Long tvShowId, Path destinationPath);
    TvShow updateTvShowById(Long tvShowId, String newTvShowName);

    void autoMoveTvShowsFromSourcePathToTvShowsPath();
    void autoGetAndUpdateTvShowsFromTvShowsPath();
    void autoCopyTvShowsFromSourcePathToTvShowsPath();

}
