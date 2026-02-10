package org.sda.mediaporter.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.TvShow;
import org.sda.mediaporter.models.metadata.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CharacterRepositoryTest {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private TvShowRepository tvShowRepository;

    private TvShow tvShow;

    @Transactional
    @BeforeEach
    void setup(){

        this.tvShow = tvShowRepository.save(TvShow.builder()
                        .title("tvShow")
                        .year(2025)
                .build());

        characterRepository.save(Character.builder()
                        .name("Character01")
                        .tvShow(tvShow)
                .build());

        characterRepository.save(Character.builder()
                        .name("Character02")
                        .tvShow(tvShow)
                .build());
    }

    @Test
    void deleteMovieCharactersIds() {
        //Act
        //Assert
    }

    @Test
    void deleteCharactersWithoutPosition() {
    }
}