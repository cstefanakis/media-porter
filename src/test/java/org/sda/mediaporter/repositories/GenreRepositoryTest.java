package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Genre action;

    @BeforeEach
    void loadData(){
        this.action = testDataFactory.createGenreAction();
    }

    @Test
    void findGenreByTitle() {
        //Arrest
        String genreTitle = this.action.getTitle();
        Long actionId = this.action.getId();
        //Act
        Optional<Genre> result = genreRepository.findGenreByTitle(genreTitle);
        //Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(actionId, result.get().getId());
    }
}