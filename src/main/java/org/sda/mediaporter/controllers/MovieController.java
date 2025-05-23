package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Servicies.MovieService;
import org.sda.mediaporter.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "http://localhost:5173")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
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
}
