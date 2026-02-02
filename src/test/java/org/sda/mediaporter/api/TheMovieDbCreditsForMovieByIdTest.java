package org.sda.mediaporter.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCrewDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbCreditsForMovieByIdTest {

    private final TheMovieDb theMovieDb = new TheMovieDb();
    private TheMovieDbCreditsForMovieById theMovieDbCreditsById;

    @BeforeEach
    void setup(){
        theMovieDbCreditsById = new TheMovieDbCreditsForMovieById(550L);
    }

    @Test
    void getActors_name() {
        //Arrest
        String actorName = "Edward Norton";

        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsById.getActors();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c-> c.getFullName().equals(actorName)));
    }

    @Test
    void getActors_gender() {
        //Arrest
        String gender = "female";

        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsById.getActors();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c-> c.getGender().equals(gender)));
    }

    @Test
    void getActors_theMovieDbId() {
        //Arrest
        Long theMovieDbId = 819L;

        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsById.getActors();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c-> c.getTheMovieDbId().equals(theMovieDbId)));
    }

    @Test
    void getActors_poster() {
        //Arrest
        String poster = theMovieDb.getPosterRootPath() + "/8nytsqL59SFJTVYVrN72k6qkGgJ.jpg";

        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsById.getActors();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c-> c.getPoster().equals(poster)));
    }

    @Test
    void getActors_character() {
        //Arrest
        String character = "Narrator";

        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsById.getActors();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c-> c.getCharacter().equals(character)));
    }

    @Test
    void getActors_order() {
        //Arrest
        Integer order = 0;

        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsById.getActors();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c-> c.getOrder().equals(order)));
    }

    @Test
    void getWriters() {
        //Arrest
        String writerName = "James Haygood";

        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsById.getWriters();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c -> c.getFullName().equals(writerName)));
    }

    @Test
    void getDirectors_directorName() {
        //Arrest
        String directorName = "David Fincher";

        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsById.getDirectors();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c -> c.getFullName().equals(directorName)));
    }

    @Test
    void getDirectors_gender() {
        //Arrest
        String gender = "male";

        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsById.getDirectors();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c -> c.getGender().equals(gender)));
    }

    @Test
    void getDirectors_theMovieDbContributorId() {
        //Arrest
        Long theMovieDbId = 7467L;

        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsById.getDirectors();

        //Assert
        assertTrue(result.stream()
                .anyMatch(c -> c.getTheMovieDbId().equals(theMovieDbId)));
    }
}