package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setup(){
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
    }

    @Test
    void isMovieByTheMovieDbIdExist() {
    }

    @Test
    void deleteMovieWithoutVideoFilePathsByMovieId_true() {
    }

    @Test
    void deleteMovieWithoutVideoFilePathsByMovieId_false() {
    }
}