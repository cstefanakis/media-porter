package org.sda.mediaporter.services.tvShowServices;

public interface TvShowEpisodeSchedulerService {
    void moveTvShowEpisodeFromDownloadsRootPathToTvShowsRootPath();
    void scanTvShowSourcePath();
    void deleteTvShowsOlderThan();
    void copyTvShowsEpisodesFromExternalSources();
}
