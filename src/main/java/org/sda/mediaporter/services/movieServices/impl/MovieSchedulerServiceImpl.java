package org.sda.mediaporter.services.movieServices.impl;

import org.sda.mediaporter.dtos.AudioDto;
import org.sda.mediaporter.dtos.VideoDto;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.services.ConfigurationService;
import org.sda.mediaporter.services.audioServices.AudioService;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.movieServices.MovieSchedulerService;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.sda.mediaporter.services.videoServices.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class MovieSchedulerServiceImpl implements MovieSchedulerService {

    private final SourcePathService sourcePathService;
    private final FileService fileService;
    private final MovieService movieService;
    private final VideoFilePathService videoFilePathService;
    private final VideoService videoService;
    private final AudioService audioService;
    private final ConfigurationService configurationService;

    @Autowired
    public MovieSchedulerServiceImpl(SourcePathService sourcePathService, FileService fileService, MovieService movieService, VideoFilePathService videoFilePathService, VideoService videoService, AudioService audioService, ConfigurationService configurationService) {
        this.sourcePathService = sourcePathService;
        this.fileService = fileService;
        this.movieService = movieService;
        this.videoFilePathService = videoFilePathService;
        this.videoService = videoService;
        this.audioService = audioService;
        this.configurationService = configurationService;
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void moveMoviesFromDownloadsRootPathToMovieRootPath(){

        SourcePath downloadsSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.DOWNLOAD, LibraryItems.MOVIE).getFirst();
        SourcePath moviesSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE).getFirst();

        Path downloadsRootPath = Path.of(downloadsSourcePath.getPath());
        Path moviesRootPath = Path.of(moviesSourcePath.getPath());

        Integer maxDatesFromDownloadPath = downloadsSourcePath.getConfiguration().getMaxDatesControlFilesFromExternalSource();

        List<Path> videoPaths = new ArrayList<>();

        if(maxDatesFromDownloadPath != null) {
            videoPaths = fileService.getVideoFiles(downloadsRootPath).stream()
                    .filter(p -> !fileService.getModificationLocalDateTimeOfPath(p).isBefore(LocalDateTime.now().minusDays(maxDatesFromDownloadPath))).toList();
        }
        createAndMoveMoviesTo(videoPaths, moviesRootPath);

    }

    void createAndMoveMoviesTo(List<Path> videoPaths, Path directory){
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
                Path newMoviePath = newMoviePath(movie, directory, fileExtension, videoFilePath);
                //update videoFilePath with new filePath

                videoFilePathService.updateSourcePathFileAndPath(videoFilePath, movie, null, newMoviePath);
                movieService.updateModificationDateTime(movie, newMoviePath);
            }
        }
    }

    private void getMovesFromVideoFilesAndCopyTo(SourcePath sourcePath ,SourcePath downloadSourcePath){
        Path sourcePathDirectory = Path.of(sourcePath.getPath());
        List<Path> videoPaths = fileService.getVideoFiles(sourcePathDirectory);
        for(Path filePath : videoPaths){
            //get a Movie
            Movie movie = movieService.getMovieFromPathFile(filePath);
            if(movie != null){
                Long movieTheMovieDbId = movie.getTheMovieDbId();
                if(!movieService.isMovieByTheMovieDbIdExist(movieTheMovieDbId)) {
                    VideoDto videoDto = videoService.getVideoDetails(filePath);
                    List<AudioDto> audiosDto = audioService.getAudiosDetails(filePath);
                    List<Genre> movieGenres = movie.getGenres();
                    double fileSize = fileService.getFileSizeInMB(filePath);
                    LocalDateTime fileModificationDateTime = fileService.getModificationLocalDateTimeOfPath(filePath);
                    if (configurationService.isFileForCopy(videoDto, audiosDto, movieGenres, sourcePath, fileSize, fileModificationDateTime)) {
                        Path filePathRoot = Path.of(sourcePath.getPath());
                        Path newPathRoot = Path.of(downloadSourcePath.getPath());
                        Path destinationFilePath = sourcePathService.replaceRootOfFilePathWithOtherRoot(filePath, filePathRoot, newPathRoot);
                        fileService.copyFile(filePath, destinationFilePath);
                    }
                }
            }
        }
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void scanMovieSourcePath() {
        SourcePath moviesSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE).getFirst();

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

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void deleteMoviesOlderThan() {
        List<SourcePath> sourcePathsSource = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE);
        for(SourcePath sourcePath : sourcePathsSource) {
            Integer days = sourcePath.getConfiguration().getMaxDatesSaveFile();
            if (days != null) {
                //get movies older than x days
                List<Long> moviesVideoFilePathsOlderThanXDays = videoFilePathService.getMoviesVideoFilesPathsOlderThanXDays(days, sourcePath);
                for (Long videoFilePathId : moviesVideoFilePathsOlderThanXDays) {
                    //Get Movie from videoFilePath
                    Long movieId = videoFilePathService.getMovieIdByVideoFilePathId(videoFilePathId);
                    //delete VideoFilePath
                    VideoFilePath videoFilePath = videoFilePathService.getVideoFilePathById(videoFilePathId);
                    movieService.deleteMovieVideoFilePath(videoFilePath);
                    //delete movie without videoFilePaths
                    movieService.deleteMovieWithoutVideoFilePathsByMovieId(movieId);
                }
            }
        }
    }



    @Override
//    @Scheduled(cron = "0 */2 * * * *")
    public void moveMoviesFromExternalSources() {
        List<SourcePath> movieExternalSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.EXTERNAL, LibraryItems.MOVIE);
        SourcePath movieRootSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE).getFirst();
        Path moviesRootPath = Path.of(movieRootSourcePath.getPath());
        movieExternalSourcePaths.forEach(sp -> {
            Path directory = Path.of(sp.getPath());
            Integer days = sp.getConfiguration().getMaxDatesControlFilesFromExternalSource();
            LocalDateTime maxDateCopyFiles = LocalDateTime.now().minusDays(days);
            List<Path> videoFiles = fileService.getVideoFilesUntil(directory, maxDateCopyFiles);
            createAndMoveMoviesTo(videoFiles, moviesRootPath);
        });

    }

    @Override
    @Scheduled(cron = "0 */2 * * * *")
    public void copyMoviesFromExternalSources() {
        videoFilePathService.deleteVideoFilePathsWithNullFilePath();
        //delete movies videoFilePaths without validated Paths
        movieService.deleteVideoFilePathFromMovieWithUnveiledPath();
        //delete movies without VideoFilePaths
        movieService.deleteMoviesWithoutVideoFilePaths();

        List<SourcePath> externalMovieSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.EXTERNAL, LibraryItems.MOVIE);
        List<SourcePath> downloadMovieSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.DOWNLOAD, LibraryItems.MOVIE);
        if(!downloadMovieSourcePaths.isEmpty()){
            SourcePath downloadSourcePath = downloadMovieSourcePaths.getFirst();
            for(SourcePath movieExternalSourcePath : externalMovieSourcePaths){
                getMovesFromVideoFilesAndCopyTo(movieExternalSourcePath, downloadSourcePath);
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
