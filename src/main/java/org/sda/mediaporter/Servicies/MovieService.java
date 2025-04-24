package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Movie;


public interface MovieService {

    Movie getMovieByTitle(String movieTitle, Integer movieYear);
}
