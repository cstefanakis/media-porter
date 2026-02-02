package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private Genre action;

    @BeforeEach
    void loadData(){
        this.action = genreRepository.save(Genre.builder()
                        .title("Action")
                .build());
    }

    @Test
    void findGenreByTitle() {
        //Arrest
        String genreTitle = "Action";
        Long actionId = this.action.getId();
        //Act
        Optional<Genre> result = genreRepository.findGenreByTitle(genreTitle);
        //Assert
        assertTrue(result.isPresent());
        assertEquals(actionId, result.get().getId());
    }
}