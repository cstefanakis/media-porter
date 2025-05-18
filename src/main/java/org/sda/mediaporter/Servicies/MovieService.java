package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.dtos.MovieUpdateDto;
import org.sda.mediaporter.models.Movie;

import java.nio.file.Path;
import java.util.List;


public interface MovieService {

    List<Movie> getMovies();
    Movie getMovieFromApiByTitle(Movie movie, String movieTitle, Integer movieYear);
    Movie getMovieById(Long movieId);
    Movie getMovieByPath(String moviePath);
    void deleteMovieById(Long id);
    Movie moveMovie(Long movieId, Path toPathWithoutFileName);
    Movie updateMovie(Long movieId, String title, Integer year);
    void copyMovie(Long movieId, Path toPathWithoutFileName);
    List<Movie> organizedDownloadMovieFiles(Path moviesDownloadPath, Path destinationPath);
    List<Movie> getMoviesFromPath(String path);
}
