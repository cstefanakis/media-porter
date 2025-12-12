package org.sda.mediaporter.services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.GenreDto;
import org.sda.mediaporter.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAllGenres();
    List<Genre> getOrCreateGenresByTitles(List<String> genresTitles);
    Genre getGenreById(Long Id);
    Genre getGenreByTitle(String title);
    Genre getOrCreateGenre(String title);
    void updateGenreById(Long id, GenreDto genreDto);
    void deleteGenreById(Long id);

    List<Genre> getGenresByTitles(List<String> genres);
}
