package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Services.MovieService;
import org.sda.mediaporter.dtos.MovieFilterDto;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "http://localhost:5173")
public class MovieController {
    private final MovieService movieService;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieService movieService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    @GetMapping()
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getMovies(pageable);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/get-movies-by-title-and-year")
    public ResponseEntity <Movie> getMovieByTitle(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "year", required = false) Integer year) {
        Movie movie = movieService.getMovieFromApiByTitle(new Movie(), title, year);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @GetMapping("/get-by-path")
    public ResponseEntity<Movie> getMovieByPath(@RequestParam(name = "path") String path) {
        Movie movie = movieService.getMovieByPath(path);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable("id") Long id) {
        movieService.deleteMovieById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/move/{id}")
    public ResponseEntity<Movie> moveMovie(@PathVariable("id") Long id,
                                           @RequestParam(name = "path") String path) {
        Path destinationPath = Paths.get(path);
        Movie movieFromDb = movieService.getMovieById(id);
        Movie movie = movieService.generateAndMoveMovieFile(movieFromDb, destinationPath);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id,
                                             @RequestParam(name = "title") String title,
                                             @RequestParam(name = "year") Integer year) {
        Movie movie = movieService.updateMovie(id, title, year);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PostMapping("/copy/{id}")
    public ResponseEntity<Void> copyMovie(@PathVariable("id") Long id,
                                          @RequestParam(name = "path") String path) {
        Path destinationPath = Paths.get(path);
        movieService.copyMovie(id, destinationPath);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/organize-download-movies")
    public ResponseEntity<Page<Movie>> organizeDownloadMovies(Pageable page,
                                                              @RequestParam(name = "sourcePath") String sourcePath,
                                                              @RequestParam(name = "destinationPath") String destinationPath) {
        Path downloadMoviesPath = Paths.get(sourcePath);
        Path destinationMoviesPath = Paths.get(destinationPath);
        Page<Movie> organizedMovies = movieService.organizedDownloadMovieFiles(page, downloadMoviesPath, destinationMoviesPath);
        return ResponseEntity.status(HttpStatus.OK).body(organizedMovies);
    }

    @GetMapping("/create-movies-from-path")
    public ResponseEntity<Page<Movie>> getMoviesFromPath(Pageable page,
                                                         @RequestParam(name = "path") String path) {
        Page<Movie> movies = movieService.getMoviesFromPath(page, path);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/get-last-five-added-movies")
    public ResponseEntity<List<Movie>> getLastFiveMovies(Pageable pageable) {
        List<Movie> movies = movieService.getFiveLastAddedMovies(pageable);
        System.out.println("Movies size: "+movies.size());
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/get-top-five-movies")
    public ResponseEntity<List<Movie>> getTopFiveMovies(Pageable pageable) {
        List<Movie> movies = movieService.getTopFiveMovies(pageable);
        return ResponseEntity.ok(movies);
    }

    @PostMapping("/filter-movies")
    public ResponseEntity<Page<Movie>> getMovieByTitle(@PageableDefault Pageable page, @RequestBody MovieFilterDto movieFilterDto) {
        Page<Movie> movies = movieService.filterMovies(page, movieFilterDto);
        return ResponseEntity.ok(movies);
    }

}
