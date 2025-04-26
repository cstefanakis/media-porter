package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Movie;


public interface MovieService {

    Movie getMovieFromApiByTitle(String movieTitle, Integer movieYear);
    Movie getMovieById(Long movieId);
    void deleteMovieById(Long id);
}
