package org.sda.mediaporter.services.tvShowServices;

public interface TvShowEpisodeSchedulerService {
    void moveTvShowEpisodeFromDownloadsRootPathToMovieRootPath();
    void scanTvShowSourcePath();
}
