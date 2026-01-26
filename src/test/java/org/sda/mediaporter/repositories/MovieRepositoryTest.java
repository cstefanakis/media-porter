package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private VideoFilePathRepository videoFilePathRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    private Movie movie;
    private Movie movieWithoutVideoFilePaths;
    private VideoFilePath movieVideoFilePath1;
    private VideoFilePath movieVideoFilePath2;
    private SourcePath sourcePath;

    @BeforeEach
    void setup(){
        this.sourcePath = sourcePathRepository.save(SourcePath.builder()
                        .libraryItem(LibraryItems.MOVIE)
                        .path("src/test/resources/files")
                        .pathType(SourcePath.PathType.EXTERNAL)
                .build());

        this.movie = movieRepository.save(Movie.builder()
                .title("movie")
                        .theMovieDbId(1L)
                .build());

        this.movieWithoutVideoFilePaths = movieRepository.save(Movie.builder()
                .title("movieWithoutVideoFilePaths")
                .theMovieDbId(2L)
                .build());

        this.movieVideoFilePath1 = videoFilePathRepository.save(VideoFilePath.builder()
                .modificationDateTime(LocalDateTime.now().minusDays(2))
                .movie(this.movie)
                        .sourcePath(this.sourcePath)
                .filePath("/notitled (720p_24fps_H264-128kbit_AAC).mp4")
                .build());
        this.movieVideoFilePath2 = videoFilePathRepository.save(VideoFilePath.builder()
                .modificationDateTime(LocalDateTime.now().minusDays(4))
                .movie(this.movie)
                        .sourcePath(this.sourcePath)
                .filePath("/Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4")
                .build());

    }

    @Test
    void findByPath() {
    }

    @Test
    void findPathMovies() {
    }

    @Test
    void findMoviesOlderThan() {
    }

    @Test
    void findLastFiveAddedMovies() {
    }

    @Test
    void findTopFiveMovies() {
    }

    @Test
    void findMovieByTitleAndYear() {
    }

    @Test
    void filterMovies() {
    }

    @Test
    void filterByAudioLanguage() {
    }

    @Test
    void deleteMoviesWithoutVideoFilePaths() {
        //Arrest
        Movie movieWithoutVideoFilePaths = movieRepository.save(Movie.builder()
                .title("movieWithoutVideoFilePaths")
                .build());
        Long movieWithoutVideoFilePathsId = movieWithoutVideoFilePaths.getId();
        //Act
        movieRepository.deleteMoviesWithoutVideoFilePaths();
        Optional<Movie> result = movieRepository.findById(movieWithoutVideoFilePathsId);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findMoviesBySourcePath() {
    }

    @Test
    void findMovieByTheMovieDbId() {
    }

    @Test
    void findMoviesOlderThanXDays() {
    }

    @Test
    void findMovieVideoFilePathsSizeByMovieId() {
        //Arrest
        Long movieId = this.movie.getId();

        //Act
        int result = movieRepository.findMovieVideoFilePathsSizeByMovieId(movieId);

        //Assert
        assertEquals(2, result);
    }

    @Test
    void isMovieByTheMovieDbIdExist() {
        //Arrest
        Long movieTheMovieDbId = this.movie.getTheMovieDbId();
        //Act
        boolean result = movieRepository.isMovieByTheMovieDbIdExist(movieTheMovieDbId);
        //Assert
        assertTrue(result);
    }

    @Test
    void deleteMovieWithoutVideoFilePathsByMovieId_true() {
        //Arrest
        Long movieId = movieWithoutVideoFilePaths.getId();
        //Act
        movieRepository.deleteMovieWithoutVideoFilePathsByMovieId(movieId);
        Optional<Movie> result = movieRepository.findById(movieId);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void deleteMovieWithoutVideoFilePathsByMovieId_false() {
        //Arrest
        Long movieId = movie.getId();
        //Act
        movieRepository.deleteMovieWithoutVideoFilePathsByMovieId(movieId);
        Optional<Movie> result = movieRepository.findById(movieId);
        //Assert
        assertTrue(result.isPresent());
    }

    @AfterEach
    void end(){
        videoFilePathRepository.delete(this.movieVideoFilePath1);
        videoFilePathRepository.delete(this.movieVideoFilePath2);
        movieRepository.delete(this.movie);
        movieRepository.delete(this.movieWithoutVideoFilePaths);
        sourcePathRepository.delete(this.sourcePath);
    }
}