package org.sda.mediaporter.services.movieServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private VideoFilePathService videoFilePathService;

    @Test
    void getMovies() {
    }

    @Test
    void getMoviesByTitleAndYear() {
    }

    @Test
    void getMovieById() {
    }

    @Test
    void getMovieByPath() {
    }

    @Test
    void getMovieFromPath_titanic() {
        //Arrest
        Path moviePath = Path.of("Z:\\Downloads\\Movies\\titanic.1997.mkv");
        Long theMovieDbId = 597L;
        String title = "Titanic";

        //Act
        Movie result = movieService.getMovieFromPathFile(moviePath);

        //Assert
        assertEquals(theMovieDbId, result.getTheMovieDbId());
        assertEquals(title, result.getTitle());
    }

    @Test
    void deleteMovieById() {
    }

    @Test
    void getMoviesFromSourcePath() {
    }

    @Test
    void getFiveLastAddedMovies() {
    }

    @Test
    void getTopFiveMovies() {
    }

    @Test
    void filterMovies() {
    }

    @Test
    void filterByAudioLanguage() {
    }

    @Test
    void deleteVideoFilePathFromMovieWithUnveiledPath() {
        //Act
        movieService.deleteVideoFilePathFromMovieWithUnveiledPath();
    }


    @Test
    void deleteMovieVideoFilePath() {
        //Arrest
        VideoFilePath movieVideoFilePath = videoFilePathService.getVideoFilePathById(2L);
        //Act
        movieService.deleteMovieVideoFilePath(movieVideoFilePath);
    }
}