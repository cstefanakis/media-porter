package org.sda.mediaporter.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbGenresTest {

    private TheMovieDbGenres genres;

    @BeforeEach
    void setup(){
        this.genres = new TheMovieDbGenres(37L);
    }

    @Test
    void getId() {
        //Arrest
        Long id = 37L;

        //Act
        Long result = genres.getId();

        //Assert
        assertEquals(id, result);
    }

    @Test
    void getName() {
        //Arrest
        String id = "Western";

        //Act
        String result = genres.getName();

        //Assert
        assertEquals(id, result);
    }
}