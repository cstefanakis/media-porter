package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.dtos.GenreResponseDto;
import org.sda.mediaporter.models.Genre;

public interface GenreService {
    Genre getGenreById(long id);
    Genre getGenreByTitle(String title);
    Genre createGenre(GenreResponseDto genreResponseDto);
    void deleteGenre(Long id);
    void updateGenre(long id, GenreResponseDto genreResponseDto);
    Genre autoCreateGenre(String title);
}
