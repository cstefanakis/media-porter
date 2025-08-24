package org.sda.mediaporter.Services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.GenreDto;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.GenreRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GenreServiceTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private GenreService genreService;

    private Genre action;
    private Genre comedy;


    @BeforeEach
    void setup(){
        configurationRepository.deleteAll();
        movieRepository.deleteAll();
        genreRepository.deleteAll();

        action = genreRepository.save(Genre.builder()
                .title("Action")
                .build());

        comedy = genreRepository.save(Genre.builder()
                .title("Comedy")
                .build());
    }

    @Test
    void autoCreateGenre() {
        //Arrest
        String title = "Crime";

        //Act
        Genre autoCreatedGenre = genreService.autoCreateGenre(title);

        //Assert
        assertEquals("Crime", genreService.getGenreByTitle(title).getTitle());
    }

    @Test
    void getAllGenres() {
        //Act
        List<Genre> genres = genreService.getAllGenres();

        //Assert
        assertEquals(2, genres.size());
    }

    @Test
    void getGenreById_existId() {
        //Arrest
        Long actionId = action.getId();

        //Act
        Genre genre = genreService.getGenreById(actionId);

        //Assert
        assertEquals("Action", genre.getTitle());
    }

    @Test
    void getGenreById_NotExistId() {
        //Arrest
        Long id = 0L;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> genreService.getGenreById(id));
    }

    @Test
    void getGenreByTitle_ExistTitle() {
        //Arrest
        String title = "ActIon";

        //Act
        Genre genre = genreService.getGenreByTitle(title);

        //Assert
        assertEquals("Action", genre.getTitle());
    }

    @Test
    void getGenreByTitle_notExistTitle() {
        //Arrest
        String title = "Anime";

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> genreService.getGenreByTitle(title));
    }

    @Test
    void createGenre_successfully() {
        //Arrest
        GenreDto genreDto = GenreDto.builder()
                .title("Adventure")
                .build();

        //Act
        Genre savedGenre = genreService.createGenre(genreDto);

        //Assert
        assertNotNull(savedGenre.getId());
        assertEquals("Adventure", savedGenre.getTitle());
    }

    @Test
    void createGenre_existTitle() {
        //Arrest
        GenreDto genreDto = GenreDto.builder()
                .title("Action")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> genreService.createGenre(genreDto));
    }

    @Test
    void createGenre_nullTitle() {
        //Arrest
        GenreDto genreDto = GenreDto.builder()
                .build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> genreService.createGenre(genreDto));
    }

    @Test
    void updateGenreById_exitGenreWithTitle() {
        //Arrest
        Long comedyId = comedy.getId();
        GenreDto genreDto = GenreDto.builder()
                .title("acTion")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> genreService.updateGenreById(comedyId, genreDto));
    }

    @Test
    void updateGenreById_withNullParameter() {
        //Arrest
        Long comedyId = comedy.getId();
        GenreDto genreDto = GenreDto.builder()
                .build();

        //Act
        genreService.updateGenreById(comedyId, genreDto);
        Genre updatedGenre = genreService.getGenreById(comedyId);

        //Assert
        assertEquals("comedy", updatedGenre.getTitle().toLowerCase());
    }

    @Test
    void deleteGenreById_successfully() {
        //Arrest
        Long comedyId = comedy.getId();

        //Act
        genreService.deleteGenreById(comedyId);

        //Assert
        assertTrue(genreRepository.findById(comedyId).isEmpty());
    }

    @Test
    void deleteGenreById_NotExistId() {
        //Arrest
        Long id = 0L;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> genreService.deleteGenreById(id));
    }
}