package org.sda.mediaporter.services.movieServices;

import org.sda.mediaporter.dtos.MovieFilterDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieSearchDTO;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.Path;
import java.util.List;


public interface MovieService {

    Page<Movie> getMovies(Pageable pageable);
    List<Movie> getMoviesByTitleAndYear(String title, Integer year);
    Movie getMovieById(Long movieId);
    Movie getMovieByPath(String moviePath);
    Movie getMovieFromPathFile(Path moviePath);
    void deleteMovieById(Long id);
    Page<Movie> getMoviesFromSourcePath(Pageable page, SourcePath sourcePath);
    Page <Movie> getFiveLastAddedMovies(Pageable pageable);
    Movie getOrCreateMovieFromPathFile(Path moviePath);
    Page<Movie> getTopFiveMovies(Pageable pageable);
    Page<Movie> filterMovies(Pageable page, MovieFilterDto movieFilterDto);
    Page<Movie> filterByAudioLanguage(Pageable page, List<Long> aLanguageIds);

    void updateModificationDateTime(Movie movie, Path filePath);

    List<Long> getMoviesIdsOlderThanXDays(int days);

    void deleteMoviesWithoutVideoFilePaths();

    void deleteVideoFilePathFromMovieWithUnveiledPath();

    void deleteMovieVideoFilePath(VideoFilePath videoFilePath);

    TheMovieDbMovieSearchDTO getMovieAPISearchDTO(String fileTitle);

    void deleteCharactersWithoutVideoFilePaths(Movie movie);

    boolean isMovieByTheMovieDbIdExist(Long movieTheMovieDbId);

    void deleteMovieWithoutVideoFilePathsByMovieId(Long movieId);
}
