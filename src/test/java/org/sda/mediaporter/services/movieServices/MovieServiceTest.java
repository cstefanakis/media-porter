package org.sda.mediaporter.services.movieServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

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
    void getMovieFromPath_OneBattleAfterAnother() {
        //Arrest
        Path moviePath = Path.of("Z:\\Downloads\\Movies\\Jedna bitva za druhou 2025.cz.dabing.Full Hd.orim..mkv");
        Long theMovieDbId = 1054867L;
        String title = "One Battle After Another";

        //Act
        Movie result = movieService.getMovieFromPathFile(moviePath);

        //Assert
        assertEquals(theMovieDbId, result.getTheMovieDbId());
        assertEquals(title, result.getTitle());
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
}