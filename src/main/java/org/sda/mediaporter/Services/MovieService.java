package org.sda.mediaporter.Services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.MovieFilterDto;
import org.sda.mediaporter.dtos.MovieUpdateDto;
import org.sda.mediaporter.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.Path;
import java.util.List;


public interface MovieService {

    Page<Movie> getMovies(Pageable pageable);
    Movie getMovieFromApiByTitle(Movie movie, String movieTitle, Integer movieYear);
    List<Movie> getMovieByTitleAndYear(String title, Integer year);
    Movie getMovieById(Long movieId);
    Movie getMovieByPath(String moviePath);
    void deleteMovieById(Long id);
    Movie moveMovie(Long movieId, Path toPathWithoutFileName);
    Movie updateMovie(Long movieId, MovieUpdateDto movieUpdateDto);
    void copyMovie(Long movieId, Path toPathWithoutFileName);
    Page<Movie> organizedDownloadMovieFiles(Pageable page, Path moviesDownloadPath, Path destinationPath);
    Page<Movie> getMoviesFromPath(Pageable page, String path);
    Movie generateAndMoveMovieFile(Movie movie, Path destinationPath);
    Page <Movie> getFiveLastAddedMovies(Pageable pageable);
//    Movie createMovie(Path file);

    Page<Movie> getTopFiveMovies(Pageable pageable);

    Page<Movie> filterMovies(Pageable page, MovieFilterDto movieFilterDto);
    Page<Movie> filterByAudioLanguage(Pageable page, List<Long> aLanguageIds);

    void moveMoviesFromDownloadPathsToMoviesPath ();
    void autoLoadMoviesFromLocalSources();
    void autoDeleteMoviesByProperties();
    void autoCopyMoviesFromExternalSource();
}
