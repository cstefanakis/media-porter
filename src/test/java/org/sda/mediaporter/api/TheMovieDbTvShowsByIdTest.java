package org.sda.mediaporter.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbTvShowsByIdTest {

    private TheMovieDbTvShowsById theWalkingDate;

    @BeforeEach
    void setup() {
        theWalkingDate = new TheMovieDbTvShowsById(1402);
    }


    @Test
    void getName() {
        //Arrest
        String name = "The Walking Dead";

        //Act
        String result = theWalkingDate.getName();

        //Assert
        assertEquals(name, result);
    }

    @Test
    void getOriginalName() {
        //Arrest
        String originalName = "The Walking Dead";

        //Act
        String result = theWalkingDate.getOriginalName();

        //Assert
        assertEquals(originalName, result);
    }

    @Test
    void getFirstAirDate() {
        //Arrest
        LocalDate firstAirDate = LocalDate.of(2010, 10, 31);

        //Act
        LocalDate result = theWalkingDate.getFirstAirDate();

        //Assert
        assertEquals(firstAirDate, result);
    }

    @Test
    void getLastAirDate() {
        //Arrest
        LocalDate lastAirDate = LocalDate.of(2022, 11, 20);

        //Act
        LocalDate result = theWalkingDate.getLastAirDate();

        //Assert
        assertEquals(lastAirDate, result);
    }

    @Test
    void getGenres() {
        //Arrest
        String action = "Action & Adventure";
        String drama = "Drama";

        //Act
        List<String> result = theWalkingDate.getGenres();

        //Assert
        assertTrue(result.contains(action));
        assertTrue(result.contains(drama));
    }

    @Test
    void getHomePage() {
        //Arrest
        String homePage = "http://www.amc.com/shows/the-walking-dead--1002293";

        //Act
        String result = theWalkingDate.getHomePage();

        //Assert
        assertEquals(homePage, result);
    }

    @Test
    void getLanguageCode() {
        //Arrest
        String languageCode = "en";

        //Act
        String result = theWalkingDate.getLanguageCode();

        //Assert
        assertEquals(languageCode, result);
    }

    @Test
    void getCountry() {
        //Arrest
        String country = "US";

        //Act
        List<String> result = theWalkingDate.getCountries();

        //Assert
        assertTrue(result.contains(country));
    }

    @Test
    void getOverview() {
        //Arrest
        String overview = "Sheriff's deputy Rick Grimes awakens from a coma to find a post-apocalyptic world dominated by flesh-eating zombies. He sets out to find his family and encounters many other survivors along the way.";

        //Act
        String result = theWalkingDate.getOverview();

        //Assert
        assertEquals(overview, result);
    }

    @Test
    void getPoster() {
        //Arrest
        String poster = "https://image.tmdb.org/t/p/w500" + "/ng3cMtxYKt1OSQYqFlnKWnVsqNO.jpg";

        //Act
        String result = theWalkingDate.getPoster();

        //Assert
        assertEquals(poster, result);
    }

    @Test
    void getStatus() {
        //Arrest
        String status = "Ended";

        //Act
        String result = theWalkingDate.getStatus();

        //Assert
        assertEquals(status, result);
    }

    @Test
    void getRate() {
        //Act
        Double result = theWalkingDate.getRate();

        //Assert
        assertTrue(result > 0);
    }
}