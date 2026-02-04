package org.sda.mediaporter.services.movieServices;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.sda.mediaporter.repositories.VideoFilePathRepository;
import org.sda.mediaporter.services.fileServices.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieSchedulerServiceTest {

    @Autowired
    private MovieSchedulerService movieSchedulerService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private VideoFilePathRepository videoFilePathRepository;

    @Autowired
    private FileService fileService;

    private Movie movie;
    private Configuration configuration;
    private VideoFilePath videoFilePath;

    @BeforeEach
    void setup(){
        Path notitled = Path.of("src/test/resources/files/movies/notitled (720p_24fps_H264-128kbit_AAC).mp4").normalize();
        Path destinationNotiledPath = Path.of("src/test/resources/downloads/movies/notitled (720p_24fps_H264-128kbit_AAC).mp4").normalize();
        Path thor = Path.of("src/test/resources/files/movies/Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4").normalize();
        Path destinationThor = Path.of("src/test/resources/files/moviessrc/test/resources/files/movies/Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4").normalize();
        Path titanic = Path.of("src/test/resources/files/movies/Titanic (1997) ¦ Official Trailer (720p_24fps_H264-128kbit_AAC).mp4").normalize();
        Path destinationTitanic = Path.of("src/test/resources/downloads/movies/Titanic (1997) ¦ Official Trailer (720p_24fps_H264-128kbit_AAC).mp4").normalize();
        try {
            if(!Files.exists(notitled)){
                Files.copy(notitled, destinationNotiledPath);
            };
            if(!Files.exists(thor)){
                Files.copy(thor, destinationThor);
            };
            if(!Files.exists(titanic)){
                Files.copy(titanic, destinationTitanic);;
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        LocalDateTime movieModificationDate = (LocalDateTime.now().minusDays(2));
        List<SourcePath> movieSourcePaths = sourcePathRepository.findSourcePathByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE);
        SourcePath movieSourcePath = new SourcePath();
        if(!movieSourcePaths.isEmpty()){
            movieSourcePath = movieSourcePaths.getFirst();
            Configuration configuration = movieSourcePath.getConfiguration();
            this.configuration = configuration;
            configuration.setMaxDatesSaveFile(1);
            configurationRepository.save(configuration);
        }

        this.movie = movieRepository.save(Movie.builder()
                        .title("Movie")
                        .lastModificationDateTime(movieModificationDate)

                .build());
        this.videoFilePath = videoFilePathRepository.save(VideoFilePath.builder()
                        .filePath(notitled.toString())
                        .modificationDateTime(movieModificationDate)
                        .sourcePath(movieSourcePath)
                        .movie(movie)
                .build());
    }

    @Test
    void moveMoviesFromDownloadsRootPathToMovieRootPath() {
        //Act
        movieSchedulerService.moveMoviesFromDownloadsRootPathToMovieRootPath();
    }

    @Test
    void scanMovieSourcePath() {
        //Act
        movieSchedulerService.scanMovieSourcePath();
    }

    @Test
    void moveMoviesFromExternalSources() {
    }

    @AfterEach
    void end(){
        Optional<Movie> movieOptional = movieRepository.findById(this.movie.getId());
        if(movieOptional.isPresent()){
            movieRepository.delete(this.movie);
        }
        Optional <Configuration> configurationOptional = configurationRepository.findById(this.configuration.getId());
        if(configurationOptional.isPresent()){
            Configuration configuration = configurationOptional.get();
            configuration.setMaxDatesSaveFile(this.configuration.getMaxDatesSaveFile());
            configurationRepository.save(configuration);
        }
        videoFilePathRepository.delete(this.videoFilePath);
    }
}