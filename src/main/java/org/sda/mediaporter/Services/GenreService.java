package org.sda.mediaporter.Services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.GenreDto;
import org.sda.mediaporter.models.Genre;

import java.util.List;

public interface GenreService {
    Genre autoCreateGenre(String title);
    List<Genre> getAllGenres();
    Genre getGenreById(Long Id);
    Genre getGenreByTitle(String title);
    Genre createGenre(@Valid GenreDto genreDto);
    void updateGenreById(Long id, GenreDto genreDto);
    void deleteGenreById(Long id);

}
