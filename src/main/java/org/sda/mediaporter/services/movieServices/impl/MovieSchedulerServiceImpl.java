package org.sda.mediaporter.services.movieServices.impl;

import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.metadata.*;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.VideoFilePathRepository;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

@Service
public class MovieSchedulerServiceImpl {

    private final SourcePathService sourcePathService;
    private final FileService fileService;
    private final MovieService movieService;
    private final VideoFilePathService videoFilePathService;

    @Autowired
    public MovieSchedulerServiceImpl(SourcePathService sourcePathService, FileService fileService, MovieService movieService, VideoFilePathService videoFilePathService) {
        this.sourcePathService = sourcePathService;
        this.fileService = fileService;
        this.movieService = movieService;
        this.videoFilePathService = videoFilePathService;
    }


    @Scheduled(cron = "0 */1 * * * *")
    public void moveMoviesFromDownloadSourceToMovieSource(){
        SourcePath downloadsSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.DOWNLOAD, LibraryItems.MOVIE).getFirst();
        SourcePath moviesSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE).getFirst();

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
                String fileExtension = fileService.getFileExtensionWithDot(filePath);
                System.out.println("file extension: " + fileExtension);
                //Create a path to move a file
                Path newMoviePath = newMoviePath(movie, moviesRootPath, fileExtension, videoFilePath);
                //update videoFilePath with new filePath
                videoFilePathService.updateSourcePathFileAndPath(videoFilePath, newMoviePath);
                movieService.updateModificationDateTime(movie, newMoviePath);
            }

        }
    }

    private Path newMoviePath(Movie movie, Path moviesRootPath, String extension, VideoFilePath videoFilePath){
        String movieTitle = fileService.getSafeFileName(movie.getTitle());
        Integer movieYear = movie.getYear();
        String movieVideo = videoFileNamePart(videoFilePath.getVideo());
        String movieAudios = audioFileNamePart(videoFilePath.getAudios());
        String movieTitleAndYear = String.format("%s (%s)", movieTitle, movieYear);
        String movieFileName = String.format("%s%s%s%s", movieTitleAndYear, movieVideo, movieAudios, extension);
        return Path.of(moviesRootPath + File.separator + movieTitleAndYear + File.separator + movieFileName);
    }

    private String audioFileNamePart(List<Audio> audios){
        if(audios == null || audios.isEmpty()){
            return "";
        }
        String audioString = "";
        for(Audio audio : audios){
            audioString = audioString + verifiedAudioString(audio) + " ";
        }
        return " (" + audioString.trim() + ")";
    }

    private String verifiedAudioString(Audio audio){
        String language = verifiedLanguage(audio);
        String channels = verifiedAudioChannel(audio);
        if(language.isBlank() && channels.isBlank()){
            return "";
        }else{
            return " [" + (channels + " " + language).trim() + "]";
        }
    }

    private String verifiedLanguage(Audio audio){
        Language language = audio.getLanguage();
        return language !=null
                ? language.getIso6392B().toUpperCase(Locale.ROOT)
                : "";
    }

    private String verifiedAudioChannel(Audio audio){
        AudioChannel audioChannel = audio.getAudioChannel();
        return audioChannel !=null
                ? audioChannel.getChannels().toString()
                : "";
    }

    private String videoFileNamePart(Video video){
        if(video == null){
            return "";
        }
        return " (" + String.format("%s %s",verifiedVideoResolution(video), verifiedVideoCodec(video)).trim() + ")";
    }

    private String verifiedVideoResolution(Video video){
        Resolution resolution = video.getResolution();
        return resolution != null
                ? fileService.getSafeFileName(resolution.getName())
                : "";
    }

    private String verifiedVideoCodec(Video video){
        Codec codec = video.getCodec();
        return codec != null
                ? fileService.getSafeFileName(codec.getName())
                : "";
    }
}
