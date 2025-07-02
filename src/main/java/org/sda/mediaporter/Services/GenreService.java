package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.Genre;

import java.util.List;

public interface GenreService {
    Genre autoCreateGenre(String title);
    List<Genre> getAllGenres();
    Genre getGenreById(Long Id);
    Genre getGenreByTitle(String title);
    Genre createGenre(String title);
    void updateGenreById(Long id, String title);
    void deleteGenreById(Long id);

}
