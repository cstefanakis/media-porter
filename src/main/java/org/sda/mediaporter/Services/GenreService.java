package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.Genre;

public interface GenreService {
    Genre autoCreateGenre(String title);
}
