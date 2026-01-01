package org.sda.mediaporter.services.movieServices.impl;

import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.metadata.*;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.movieServices.MovieSchedulerService;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Service
public class MovieSchedulerServiceImpl implements MovieSchedulerService {

    private final SourcePathService sourcePathService;
    private final FileService fileService;
    private final MovieService movieService;
    private final VideoFilePathService videoFilePathService;

    private final SourcePath downloadsSourcePath;
    private final SourcePath moviesSourcePath;

    @Autowired
    public MovieSchedulerServiceImpl(SourcePathService sourcePathService, FileService fileService, MovieService movieService, VideoFilePathService videoFilePathService) {
        this.sourcePathService = sourcePathService;
        this.fileService = fileService;
        this.movieService = movieService;
        this.videoFilePathService = videoFilePathService;

        this.downloadsSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.DOWNLOAD, LibraryItems.MOVIE).getFirst();
        this.moviesSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE).getFirst();
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void moveMoviesFromDownloadsRootPathToMovieRootPath(){

        Path downloadsRootPath = Path.of(downloadsSourcePath.getPath());
        Path moviesRootPath = Path.of(moviesSourcePath.getPath());

        List<Path> videoPaths = fileService.getVideoFiles(downloadsRootPath);

        for(Path filePath : videoPaths){
            //Create a Movie
            Movie movie = movieService.getOrCreateMovieFromPathFile(filePath);
            if(movie != null){
                //create VideoFilePath
                VideoFilePath videoFilePath = videoFilePathService.createVideoFilePath(filePath);
                //Add movie to VideoFilePath
                videoFilePathService.addMovie(movie, videoFilePath);
                //get extension of a file;
                String fileNameOfPath = filePath.getFileName().toString();
                String fileExtension = fileService.getFileExtensionWithDot(fileNameOfPath);
                //Create a path to move a file
                Path newMoviePath = newMoviePath(movie, moviesRootPath, fileExtension, videoFilePath);
                //update videoFilePath with new filePath

                videoFilePathService.updateSourcePathFileAndPath(videoFilePath, movie, null, newMoviePath);
                movieService.updateModificationDateTime(movie, newMoviePath);
            }

        }
    }

    @Override
    @Scheduled(cron = "0 0 */12 * * *")
    public void scanMovieSourcePath() {
        Path moviesRootPath = Path.of(moviesSourcePath.getPath());

        List<Path> videoPaths = fileService.getVideoFiles(moviesRootPath);
        for(Path filePath : videoPaths){
            String filePathWithoutMovieSourcePath = videoFilePathService.getFilePathWithoutSourcePath(filePath, moviesSourcePath);
            if(movieService.getMovieByPath(filePathWithoutMovieSourcePath)== null){
                //Create a Movie
                Movie movie = movieService.getOrCreateMovieFromPathFile(filePath);
                //create VideoFilePath
                VideoFilePath videoFilePath = videoFilePathService.createVideoFilePath(filePath);
                //Add movie to VideoFilePath
                videoFilePathService.addMovie(movie, videoFilePath);
            }
        }
    }

    private Path newMoviePath(Movie movie, Path moviesRootPath, String extension, VideoFilePath videoFilePath){
        String movieTitle = fileService.getSafeFileName(movie.getTitle());
        Integer movieYear = movie.getYear();
        String movieVideo = videoFilePathService.getVideoFileNamePart(videoFilePath.getVideo());
        String movieAudios = videoFilePathService.getAudioFileNamePart(videoFilePath.getAudios());
        String movieTitleAndYear = String.format("%s (%s)", movieTitle, movieYear);
        String movieFileName = String.format("%s%s%s%s", movieTitleAndYear, movieVideo, movieAudios, extension);
        return Path.of(moviesRootPath + File.separator + movieTitleAndYear + File.separator + movieFileName);
    }
}
