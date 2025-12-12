package org.sda.mediaporter.services.tvShowServices;

import org.sda.mediaporter.models.TvShowEpisode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.Path;

public interface TvShowEpisodeService {
    TvShowEpisode getTvShowEpisodeById(Long Id);
    boolean deleteTvShowEpisodeById(Long id);
    TvShowEpisode updateTvShowEpisodeById(Long id);
    Page<TvShowEpisode> getTvShowEpisodesByTvShowIdAndSeason(Long tvShowId,Integer season, Pageable pageable);
    TvShowEpisode createTvShowEpisodeFromPath(Path videoFilePath);

    void updateModificationDateTime(TvShowEpisode tvShowEpisode, Path newTvShowEpisodePath);
}
