package org.sda.mediaporter.controllers;

import jakarta.validation.Valid;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.sda.mediaporter.dtos.MovieFilterDto;
import org.sda.mediaporter.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping()
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getMovies(pageable);
        return ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/get-by-title-and-year")
    public ResponseEntity<List<Movie>> getMoviesByTitleAndYear(@RequestParam("title") String title,
                                                               @RequestParam("year") Integer year){
        List<Movie> movies = movieService.getMoviesByTitleAndYear(title, year);
        return ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/get-by-path")
    public ResponseEntity<Movie> getMovieByPath(@RequestParam(name = "path") String path) {
        Movie movie = movieService.getMovieByPath(path);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable("id") Long id) {
        movieService.deleteMovieById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/get-last-five-added-movies")
    public ResponseEntity<Page<Movie>> getLastFiveMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getFiveLastAddedMovies(pageable);
        return ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasAnyRole('USER', ADMIN)")
    @GetMapping("/get-top-five-movies")
    public ResponseEntity<Page<Movie>> getTopFiveMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getTopFiveMovies(pageable);
        return ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/filter-movies")
    public ResponseEntity<Page<Movie>> getMovieByTitle(@PageableDefault Pageable page,
                                                       @RequestBody @Valid MovieFilterDto movieFilterDto) {
        Page<Movie> movies = movieService.filterMovies(page, movieFilterDto);
        return ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/filter-movies-by-audio-languages")
    public ResponseEntity<Page<Movie>> filterByAudioLanguage(@PageableDefault Pageable page,
                                                             @RequestParam("languages") List<Long> languages) {
        Page<Movie> movies = movieService.filterByAudioLanguage(page, languages);
        return ResponseEntity.ok(movies);
    }

}
