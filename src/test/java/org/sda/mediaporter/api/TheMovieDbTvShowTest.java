package org.sda.mediaporter.api;

import org.junit.jupiter.api.BeforeEach;
import org.sda.mediaporter.models.Country;
import org.sda.mediaporter.models.Genre;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbTvShowTest {

    private TheMovieDbTvShow dexterResurrection;

    @BeforeEach
        void setup(){
            dexterResurrection = new TheMovieDbTvShow("Dexter Resurrection");
        }


    @org.junit.jupiter.api.Test
    void id() {
        //Arrest
        Long id = 259909L;

        //Act
        Long result = dexterResurrection.getId();

        //Assert
        assertEquals(id, result);
    }

    @org.junit.jupiter.api.Test
    void originalName() {
        //Arrest
        String originalName = "Dexter: Resurrection";

        //Act
        String result = dexterResurrection.getOriginalName();

        //Assert
        assertEquals(originalName, result);
    }

    @org.junit.jupiter.api.Test
    void poster() {
        //Arrest
        String poster = "https://image.tmdb.org/t/p/w500" + "/hIawSocuwqgNeRf3JuKuxgMHmSC.jpg";

        //Act
        String result = dexterResurrection.getPoster();

        //Assert
        assertEquals(poster, result);
    }

    @org.junit.jupiter.api.Test
    void overview() {
        //Arrest
        String overview = "Dexter Morgan awakens from a coma to find Harrison gone without a trace. Realizing the weight of what he put his son through, Dexter sets out for New York City, determined to find him and make things right. But closure won't come easy. When Miami Metro's Angel Batista arrives with questions, Dexter realizes his past is catching up to him fast. As father and son navigate their own darkness in the city that never sleeps, they soon find themselves deeper than they ever imagined - and that the only way out is together.";

        //Act
        String result = dexterResurrection.getOverview();

        //Assert
        assertEquals(overview, result);
    }

    @org.junit.jupiter.api.Test
    void originalLanguage() {
        //Arrest
        String originalLanguage = "en";

        //Act
        String result = dexterResurrection.getOriginalLanguage();

        //Assert
        assertEquals(originalLanguage, result);
    }

    @org.junit.jupiter.api.Test
    void firstAirDate() {
        //Arrest
        LocalDate firstAirDate = LocalDate.of(2025, 7, 13);

        //Act
        LocalDate result = dexterResurrection.getFirstAirDate();

        //Assert
        assertEquals(firstAirDate, result);
    }

    @org.junit.jupiter.api.Test
    void rate() {
        //Arrest
        double rate = 5;

        //Act
        Double result = dexterResurrection.getRate();

        //Assert
        assertTrue(result > rate);
    }

    @org.junit.jupiter.api.Test
    void countries() {
        //Arrest
        String country = "US";

        //Act
        List<String> result = dexterResurrection.getCountries();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c-> c.equals(country)));
    }

    @org.junit.jupiter.api.Test
    void genre() {
        //Arrest
        String genre = "Crime";

        //Act
        List<String> result = dexterResurrection.getGenres();

        //Assert
        assertTrue(result.stream().anyMatch(g -> g.equals(genre)));
    }
}