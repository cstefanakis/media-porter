package org.sda.mediaporter.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CharacterRepositoryTest {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private MovieRepository movieRepository;

    private Movie movie;

    @Transactional
    @BeforeEach
    void setup(){

        movie = movieRepository.save(Movie.builder()
                .title("Movie")
                .build());


        characterRepository.save(Character.builder()
                        .name("Character01")
                        .movie(movie)
                .build());

        characterRepository.save(Character.builder()
                        .name("Character02")
                        .movie(movie)
                .build());
    }

    @Test
    void deleteMovieCharactersIds() {
        //Act
        characterRepository.deleteMovieCharactersIds(movie);
        //Assert
        assertTrue(movie.getCharacters().isEmpty());
    }

    @AfterEach
    void end(){
        movieRepository.delete(movie);
        characterRepository.deleteCharactersWithoutPosition();
    }
}