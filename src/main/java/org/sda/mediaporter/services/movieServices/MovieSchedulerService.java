package org.sda.mediaporter.services.movieServices;

public interface MovieSchedulerService {
    void moveMoviesFromDownloadsRootPathToMovieRootPath();
    void scanMovieSourcePath();
    void deleteMoviesOlderThan();
    void moveMoviesFromExternalSources();
    void copyMoviesFromExternalSources();
}
