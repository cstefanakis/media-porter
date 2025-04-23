package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Movie;

import java.util.List;

public interface MovieService {

    Movie getMovieByTitle(String movieTitle, Integer movieYear);
}
