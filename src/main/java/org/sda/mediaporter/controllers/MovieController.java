package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Servicies.Impl.MovieServiceImpl;
import org.sda.mediaporter.Servicies.MovieService;
import org.sda.mediaporter.models.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/generate")
    public ResponseEntity <Movie> getMovieByTitle(
            @RequestParam String title,
            @RequestParam(required = false) Integer year) {
        Movie movie = movieService.getMovieByTitle(title, year);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }
}
