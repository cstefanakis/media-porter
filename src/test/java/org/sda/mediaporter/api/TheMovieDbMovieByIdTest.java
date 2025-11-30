package org.sda.mediaporter.api;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbMovieByIdTest {

    private final TheMovieDbMovieById theMovieDbMovieById = new TheMovieDbMovieById(550L);
    private final TheMovieDb theMovieDb = new TheMovieDb();

    @Test
    void getTheMovieDbMovieDto_title() {
        //Arrest
        String title = "Fight Club";
        //Act
        String result = theMovieDbMovieById.getTheMovieDbMovieDto().getTitle();
        //Assert
        assertEquals(title, result);
    }

    @Test
    void getTheMovieDbMovieDto_originalTitle() {
        //Arrest
        String originalTitle = "Fight Club";
        //Act
        String result = theMovieDbMovieById.getTheMovieDbMovieDto().getOriginalTitle();
        //Assert
        assertEquals(originalTitle, result);
    }

    @Test
    void getTheMovieDbMovieDto_year() {
        //Arrest
        Integer year = 1999;
        //Act
        Integer result = theMovieDbMovieById.getTheMovieDbMovieDto().getYear();
        //Assert
        assertEquals(year, result);
    }

    @Test
    void getTheMovieDbMovieDto_genres() {
        //Arrest
        String genre = "Drama";
        //Act
        List<String> result = theMovieDbMovieById.getTheMovieDbMovieDto().getGenres();
        //Assert
        assertTrue(result.stream()
                .anyMatch(genre::equals));
    }

    @Test
    void getTheMovieDbMovieDto_homePage() {
        //Arrest
        String homePage = "http://www.foxmovies.com/movies/fight-club";
        //Act
        String result = theMovieDbMovieById.getTheMovieDbMovieDto().getHomePage();
        //Assert
        assertEquals(homePage, result);
    }

    @Test
    void getTheMovieDbMovieDto_theMovieDbId() {
        //Arrest
        Long theMovieDbId = 550L;
        //Act
        Long result = theMovieDbMovieById.getTheMovieDbMovieDto().getTheMoveDbId();
        //Assert
        assertEquals(theMovieDbId, result);
    }

    @Test
    void getTheMovieDbMovieDto_countries() {
        //Arrest
        String countryCode = "US";
        //Act
        List<String> result = theMovieDbMovieById.getTheMovieDbMovieDto().getCountries();
        //Assert
        assertTrue(result.stream()
                .anyMatch(code -> code.equals(countryCode)));
    }

    @Test
    void getTheMovieDbMovieDto_languageCode() {
        //Arrest
        String languageCode = "en";
        //Act
        String result = theMovieDbMovieById.getTheMovieDbMovieDto().getLanguageCode();
        //Assert
        assertEquals(languageCode, result);
    }

    @Test
    void getTheMovieDbMovieDto_overview() {
        //Arrest
        String overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.";
        //Act
        String result = theMovieDbMovieById.getTheMovieDbMovieDto().getOverview();
        //Assert
        assertEquals(overview, result);
    }

    @Test
    void getTheMovieDbMovieDto_poster() {
        //Arrest
        String poster = theMovieDb.getPosterRootPath() + "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg";
        //Act
        String result = theMovieDbMovieById.getTheMovieDbMovieDto().getPoster();
        //Assert
        assertEquals(poster, result);
    }

    @Test
    void getTheMovieDbMovieDto_status() {
        //Arrest
        String status = "Released";
        //Act
        String result = theMovieDbMovieById.getTheMovieDbMovieDto().getStatus();
        //Assert
        assertEquals(status, result);
    }

    @Test
    void getTheMovieDbMovieDto_rate() {

        //Act
        Double result = theMovieDbMovieById.getTheMovieDbMovieDto().getRate();
        //Assert
        assertTrue(result > 0);
    }

    @Test
    void getTheMovieDbMovieDto_releaseDate() {
        LocalDate releaseDate = LocalDate.of(1999, 10, 15);
        //Act
        LocalDate result = theMovieDbMovieById.getTheMovieDbMovieDto().getReleaseDate();
        //Assert
        assertEquals(releaseDate, result);
    }

}