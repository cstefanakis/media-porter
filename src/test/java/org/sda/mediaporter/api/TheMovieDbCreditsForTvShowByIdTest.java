package org.sda.mediaporter.api;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCrewDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbCreditsForTvShowByIdTest {
    private final TheMovieDbCreditsForTvShowById theMovieDbCreditsForTvShowById = new TheMovieDbCreditsForTvShowById(550L);
    private final TheMovieDb theMovieDb = new TheMovieDb();

    @Test
    void getActors_fullName() {
        //Arrest
        String fullName = "Warren Mitchell";
        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsForTvShowById.getActors();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCastDto -> tmdCastDto.getFullName().equals(fullName)));
    }

    @Test
    void getActors_gender() {
        //Arrest
        String gender = "male";
        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsForTvShowById.getActors();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCastDto -> tmdCastDto.getGender().equals(gender)));
    }

    @Test
    void getActors_theMovieDbId() {
        //Arrest
        Long theMovieDbId = 94713L;
        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsForTvShowById.getActors();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCastDto -> tmdCastDto.getTheMovieDbId().equals(theMovieDbId)));
    }

    @Test
    void getActors_poster() {
        //Arrest
        String poster = theMovieDb.getPosterRootPath() + "/mMFruuo24m0A8LgVXjE6cUDrGFx.jpg";
        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsForTvShowById.getActors();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCastDto -> tmdCastDto.getPoster().equals(poster)));
    }

    @Test
    void getActors_character() {
        //Arrest
        String character = "Alf Garnett";
        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsForTvShowById.getActors();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCastDto -> tmdCastDto.getCharacter().equals(character)));
    }

    @Test
    void getActors_order() {
        //Arrest
        Integer order = 0;
        //Act
        List<TheMovieDbCastDto> result = theMovieDbCreditsForTvShowById.getActors();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCastDto -> tmdCastDto.getOrder().equals(order)));
    }

    @Test
    void getWriters_fullName() {
        //Arrest
        String fullName = "Johnny Speight";
        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsForTvShowById.getWriters();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCrewDto -> tmdCrewDto.getFullName().equals(fullName)));

    }

    @Test
    void getWriters_gender() {
        //Arrest
        String gender = "male";
        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsForTvShowById.getWriters();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCrewDto -> tmdCrewDto.getGender().equals(gender)));

    }

    @Test
    void getWriters_poster() {
        //Arrest
        String poster = null;
        String fullName = "Johnny Speight";
        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsForTvShowById.getWriters();
        //Assert
        assertNull(result.stream()
                .filter(w -> w.getFullName().equals(fullName))
                .findFirst().get().getPoster());

    }

    @Test
    void getWriters_theMovieDbId() {
        //Arrest
        Long theMovieDbId = 32568L;
        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsForTvShowById.getWriters();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCrewDto -> tmdCrewDto.getTheMovieDbId().equals(theMovieDbId)));

    }

    @Test
    void getWriters_department() {
        //Arrest
        String department = "Writing";
        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsForTvShowById.getWriters();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCrewDto -> tmdCrewDto.getDepartment().equals(department)));

    }

    @Test
    void getWriters_job() {
        //Arrest
        String job = "Writer";
        //Act
        List<TheMovieDbCrewDto> result = theMovieDbCreditsForTvShowById.getWriters();
        //Assert
        assertTrue(result.stream()
                .anyMatch(tmdCrewDto -> tmdCrewDto.getJob().equals(job)));

    }

    @Test
    void getDirectors() {
        
    }
}