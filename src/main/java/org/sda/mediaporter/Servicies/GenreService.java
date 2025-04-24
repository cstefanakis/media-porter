package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Genre;

public interface GenreService {
    Genre getGenreById(long id);
    Genre autoCreateGenre(String title);
}
