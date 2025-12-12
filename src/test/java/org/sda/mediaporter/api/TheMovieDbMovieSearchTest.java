package org.sda.mediaporter.api;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieSearchDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbMovieSearchTest {

    private TheMovieDbMovieSearch titanicSearch = new TheMovieDbMovieSearch("titanic", 1997);
    private TheMovieDb theMovieDb = new TheMovieDb();

    @Test
    void getMoviesSearchFromApi_testOriginalTitle() {
        //Arrest
        String originalTitle1 = "Titanic";
        String originalTitle2 = "La femme de chambre du Titanic";
        String originalTitle3 = "Titanic: Anatomy of a Disaster";

        //Act
        List<TheMovieDbMovieSearchDTO> result = titanicSearch.getMoviesSearchFromApi();

        //Assert
        assertTrue(result.stream()
                .anyMatch(s -> s.getOriginalTitle().equals(originalTitle1)));
        assertTrue(result.stream()
                .anyMatch(s -> s.getOriginalTitle().equals(originalTitle2)));
        assertTrue(result.stream()
                .anyMatch(s -> s.getOriginalTitle().equals(originalTitle3)));
    }

    @Test
    void getMoviesSearchFromApi_title() {
        //Arrest
        String title = "Titanic";

        //Act
        List<TheMovieDbMovieSearchDTO> result = titanicSearch.getMoviesSearchFromApi();

        //Assert
        assertTrue(result.stream()
                .anyMatch(s -> s.getTitle().equals(title)));

    }

    @Test
    void getMoviesSearchFromApi_overview() {
        //Arrest
        String overview = "101-year-old Rose DeWitt Bukater tells the story of her life aboard the Titanic, 84 years later. A young Rose boards the ship with her mother and fiancé. Meanwhile, Jack Dawson and Fabrizio De Rossi win third-class tickets aboard the ship. Rose tells the whole story from Titanic's departure through to its death—on its first and last voyage—on April 15, 1912.";

        //Act
        List<TheMovieDbMovieSearchDTO> result = titanicSearch.getMoviesSearchFromApi();

        //Assert
        assertTrue(result.stream()
                .anyMatch(s -> s.getOverview().equals(overview)));

    }

    @Test
    void getMoviesSearchFromApi_theMovieDbId() {
        //Arrest
        Long theMovieDbId = 597L;

        //Act
        List<TheMovieDbMovieSearchDTO> result = titanicSearch.getMoviesSearchFromApi();

        //Assert
        assertTrue(result.stream()
                .anyMatch(s -> s.getTheMovieDbId().equals(theMovieDbId)));

    }

    @Test
    void getMoviesSearchFromApi_posterPath() {
        //Arrest
        String posterPath = theMovieDb.getPosterRootPath() + "/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg";

        //Act
        List<TheMovieDbMovieSearchDTO> result = titanicSearch.getMoviesSearchFromApi();

        //Assert
        assertTrue(result.stream()
                .anyMatch(s -> s.getPosterPath().equals(posterPath)));

    }

    @Test
    void getMoviesSearchFromApi_year() {
        //Arrest
        Integer year = 1997;

        //Act
        List<TheMovieDbMovieSearchDTO> result = titanicSearch.getMoviesSearchFromApi();

        //Assert
        assertTrue(result.stream()
                .anyMatch(s -> s.getYear().equals(year)));

    }

    @Test
    void getMoviesSearchFromApi_originalLanguage() {
        //Arrest
        String originalLanguageCode = "en";

        //Act
        List<TheMovieDbMovieSearchDTO> result = titanicSearch.getMoviesSearchFromApi();

        //Assert
        assertTrue(result.stream()
                .anyMatch(s -> s.getOriginalLanguage().equals(originalLanguageCode)));

    }
}