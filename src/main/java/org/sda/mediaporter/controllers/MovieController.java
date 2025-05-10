package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Servicies.MovieService;
import org.sda.mediaporter.dtos.MovieUpdateDto;
import org.sda.mediaporter.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/get-movies-by-title-and-year")
    public ResponseEntity <Movie> getMovieByTitle(
            @RequestParam String title,
            @RequestParam(required = false) Integer year) {
        Movie movie = movieService.getMovieFromApiByTitle(title, year);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @GetMapping("/get-by-path")
    public ResponseEntity<Movie> getMovieByPath(@RequestParam String path) {
        Movie movie = movieService.getMovieByPath(path);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable Long id) {
        movieService.deleteMovieById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/move/{id}")
    public ResponseEntity<Movie> moveMovie(@PathVariable Long id, @RequestParam String path) {
        Path destinationPath = Paths.get(path);
        Movie movie = movieService.moveMovie(id, destinationPath);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody MovieUpdateDto movieUpdateDto) {
        Movie movie = movieService.updateMovie(id, movieUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PostMapping("/copy/{id}")
    public ResponseEntity<Void> copyMovie(@PathVariable Long id, @RequestParam String path) {
        Path destinationPath = Paths.get(path);
        movieService.copyMovie(id, destinationPath);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/organize-download-movies")
    public ResponseEntity<List<Movie>> organizeDownloadMovies(@RequestParam String downloadPath, @RequestParam String destinationPath) {
        Path downloadMoviesPath = Paths.get(downloadPath);
        Path destinationMoviesPath = Paths.get(destinationPath);
        List<Movie> organizedMovies = movieService.organizedDownloadMovieFiles(downloadMoviesPath, destinationMoviesPath);
        return ResponseEntity.status(HttpStatus.OK).body(organizedMovies);
    }

    @PostMapping("/create-movies-from-path")
    public ResponseEntity<List<Movie>> getMoviesFromPath(@RequestParam(name = "path") String path) {
        List<Movie> movies = movieService.getMoviesFromPath(path);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }
}
