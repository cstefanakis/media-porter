package org.sda.mediaporter.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.TvShow;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbTvShowSearchTest {

    private TheMovieDbTvShowSearch dexter;
    private TheMovieDbTvShowSearch dexterResurrectionCz;
    private TheMovieDbTvShowSearch dexterResurrectionEn;

    @BeforeEach
    void setup(){
        dexter = new TheMovieDbTvShowSearch("Dexter");
        dexterResurrectionCz = new TheMovieDbTvShowSearch("Dexter Vzkříšení ");
        dexterResurrectionEn = new TheMovieDbTvShowSearch("Dexter Resurrection ");
    }

    @Test
    void getTvShows() {
        //Assert
        String title = "Dexter";
        String originalName = "Dexter";
        int year = 2006;

        //Act
        List<TvShow> result = dexter.getTvShows();

        //Arrest
        assertTrue(result.stream()
                .anyMatch(t -> t.getTitle().equals(title)));
        assertTrue(result.stream()
                .anyMatch(ot -> ot.getOriginalTitle().equals(originalName)));
        assertTrue(result.stream()
                .anyMatch(y -> y.getYear().equals(year)));
        assertTrue(result.stream()
                .anyMatch(obj -> obj.getId() == 1405));
        assertTrue(result.stream()
                .filter(tvShow -> tvShow.getId() == null).toList().isEmpty());
    }

    @Test
    void getTvShows_czCharactersAndSpace() {
        //Assert
        String title = "Dexter: Resurrection";
        String originalName = "Dexter: Resurrection";
        int year = 2025;

        //Act
        List<TvShow> result = dexterResurrectionCz.getTvShows();

        //Arrest
        assertTrue(result.stream()
                .anyMatch(t -> t.getTitle().equals(title)));
        assertTrue(result.stream()
                .anyMatch(ot -> ot.getOriginalTitle().equals(originalName)));
        assertTrue(result.stream()
                .anyMatch(y -> y.getYear().equals(year)));
        assertTrue(result.stream()
                .anyMatch(obj -> obj.getId() == 259909));
        assertTrue(result.stream()
                .filter(tvShow -> tvShow.getId() == null).toList().isEmpty());
    }

    @Test
    void getTvShows_enCharactersAndSpace() {
        //Assert
        String title = "Dexter: Resurrection";
        String originalName = "Dexter: Resurrection";
        int year = 2025;

        //Act
        List<TvShow> result = dexterResurrectionEn.getTvShows();

        //Arrest
        assertTrue(result.stream()
                .anyMatch(t -> t.getTitle().equals(title)));
        assertTrue(result.stream()
                .anyMatch(ot -> ot.getOriginalTitle().equals(originalName)));
        assertTrue(result.stream()
                .anyMatch(y -> y.getYear().equals(year)));
        assertTrue(result.stream()
                .anyMatch(obj -> obj.getId() == 259909));
        assertTrue(result.stream()
                .filter(tvShow -> tvShow.getId() == null).toList().isEmpty());
    }
}