package org.sda.mediaporter.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowSearchDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbTvShowSearchTest {

    private TheMovieDbTvShowSearch dexter;
    private TheMovieDbTvShowSearch dexterResurrectionCz;
    private TheMovieDbTvShowSearch dexterResurrectionEn;
    private TheMovieDbTvShowSearch it;

    @BeforeEach
    void setup(){
        dexter = new TheMovieDbTvShowSearch("Dexter");
        dexterResurrectionCz = new TheMovieDbTvShowSearch("Dexter Vzkříšení ");
        dexterResurrectionEn = new TheMovieDbTvShowSearch("Dexter Resurrection ");
        it = new TheMovieDbTvShowSearch("IT Welcome to Derry");
    }

    @Test
    void getTvShows() {
        //Assert
        String title = "Dexter";
        String originalName = "Dexter";
        int year = 2006;

        //Act
        List<TheMovieDbTvShowSearchDTO> result = dexter.getTvShows();

        //Arrest
        assertTrue(result.stream()
                .anyMatch(t -> t.getTitle().equals(title)));
        assertTrue(result.stream()
                .anyMatch(ot -> ot.getOriginalTitle().equals(originalName)));
        assertTrue(result.stream()
                .anyMatch(y -> y.getYear().equals(year)));
        assertTrue(result.stream()
                .anyMatch(obj -> obj.getTheMovieDbId() == 1405));
        assertTrue(result.stream()
                .filter(tvShow -> tvShow.getTheMovieDbId() == null).toList().isEmpty());
    }

    @Test
    void getTvShows_czCharactersAndSpace() {
        //Assert
        String title = "Dexter: Resurrection";
        String originalName = "Dexter: Resurrection";
        int year = 2025;

        //Act
        List<TheMovieDbTvShowSearchDTO> result = dexterResurrectionCz.getTvShows();

        //Arrest
        assertTrue(result.stream()
                .anyMatch(t -> t.getTitle().equals(title)));
        assertTrue(result.stream()
                .anyMatch(ot -> ot.getOriginalTitle().equals(originalName)));
        assertTrue(result.stream()
                .anyMatch(y -> y.getYear().equals(year)));
        assertTrue(result.stream()
                .anyMatch(obj -> obj.getTheMovieDbId() == 259909));
        assertTrue(result.stream()
                .filter(tvShow -> tvShow.getTheMovieDbId() == null).toList().isEmpty());
    }

    @Test
    void getTvShows_enCharactersAndSpace() {
        //Assert
        String title = "Dexter: Resurrection";
        String originalName = "Dexter: Resurrection";
        int year = 2025;

        //Act
        List<TheMovieDbTvShowSearchDTO> result = dexterResurrectionEn.getTvShows();

        //Arrest
        assertTrue(result.stream()
                .anyMatch(t -> t.getTitle().equals(title)));
        assertTrue(result.stream()
                .anyMatch(ot -> ot.getOriginalTitle().equals(originalName)));
        assertTrue(result.stream()
                .anyMatch(y -> y.getYear().equals(year)));
        assertTrue(result.stream()
                .anyMatch(obj -> obj.getTheMovieDbId() == 259909));
        assertTrue(result.stream()
                .filter(tvShow -> tvShow.getTheMovieDbId() == null).toList().isEmpty());
    }

    @Test
    void getTvShows_testTheMoveDbId() {
        //Assert
        Long theMovieId = 200875L;

        //Act
        List<TheMovieDbTvShowSearchDTO> result = it.getTvShows();

        //Arrest
        assertTrue(result.stream()
                .anyMatch(obj -> obj.getTheMovieDbId().equals(theMovieId)));
    }
}