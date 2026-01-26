package org.sda.mediaporter.services.tvShowServices;

import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowEpisodeDto;
import org.sda.mediaporter.models.TvShowEpisode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.Path;
import java.util.Optional;

public interface TvShowEpisodeService {
    TvShowEpisode getTvShowEpisodeById(Long Id);
    boolean deleteTvShowEpisodeById(Long id);
    TvShowEpisode updateTvShowEpisodeById(Long id);
    Page<TvShowEpisode> getTvShowEpisodesByTvShowIdAndSeason(Long tvShowId,Integer season, Pageable pageable);
    TvShowEpisode createTvShowEpisodeFromPath(Path videoFilePath);

    void updateModificationDateTime(TvShowEpisode tvShowEpisode, Path newTvShowEpisodePath);

    TvShowEpisode getTvShowEpisodeByPathOrNull(String filePathWithoutTvShowSourcePath);

    Long getTvShowEpisodeIdByVideoFilePathId(Long videoFilePathId);

    void deleteTvShowEpisodeWithoutVideoFilePaths(Long tvShowEpisodeId);

    void deleteTvShowEpisodesWithoutVideoFilePaths();

    TheMovieDbTvShowEpisodeDto getTvShowEpisodeDtoByPath(Path filePath);

    boolean isExistTvShowEpisodeWithTheMovieDbId(Long tvShowEpisodeTheMovieDbId);
}
