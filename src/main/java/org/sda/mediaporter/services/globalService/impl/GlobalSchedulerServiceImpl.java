package org.sda.mediaporter.services.globalService.impl;

import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.globalService.GlobalSchedulerService;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GlobalSchedulerServiceImpl implements GlobalSchedulerService {

    private final VideoFilePathService videoFilePathService;
    private final MovieService movieService;
    private final SourcePathService sourcePathService;
    private final FileService fileService;

    @Autowired
    public GlobalSchedulerServiceImpl(VideoFilePathService videoFilePathService, MovieService movieService, SourcePathService sourcePathService, FileService fileService) {
        this.videoFilePathService = videoFilePathService;
        this.movieService = movieService;
        this.sourcePathService = sourcePathService;
        this.fileService = fileService;
    }

    @Override
    @Scheduled(cron = "0 */2 * * * *")
    public void cleanVideoFilePaths() {
        //delete videoFilePaths with null file path
        videoFilePathService.deleteVideoFilePathsWithNullFilePath();
        //delete videoFilePaths without validated Paths
        movieService.deleteVideoFilePathFromMovieWithUnveiledPath();
        //delete movies without VideoFilePaths
        movieService.deleteMoviesWithoutVideoFilePaths();
        //delete all empty directories
        List<SourcePath> moviesSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE);
        List<SourcePath> tvShowSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.TV_SHOW);
        List<SourcePath> downloadMovieSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.DOWNLOAD, LibraryItems.MOVIE);
        List<SourcePath> downloadTvShowSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.DOWNLOAD, LibraryItems.TV_SHOW);
        List<SourcePath> sourcePaths = Stream.of(
                moviesSourcePaths,
                        downloadMovieSourcePaths,
                        downloadTvShowSourcePaths,
                        tvShowSourcePaths)
                .flatMap(List::stream)
                .toList();

        sourcePaths.forEach(sp -> {
            Path rootDirectory = Path.of(sp.getPath());
            fileService.deleteAllDirectoriesWithoutVideFiles(rootDirectory);
            fileService.deleteAllEmptyDirectories(rootDirectory);
        });
    }
}
