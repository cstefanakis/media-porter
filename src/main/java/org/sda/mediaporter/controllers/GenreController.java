package org.sda.mediaporter.controllers;

import org.apache.coyote.Response;
import org.sda.mediaporter.Services.GenreService;
import org.sda.mediaporter.models.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping()
    public ResponseEntity<List<Genre>> getAllGenres(){
        List<Genre> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/id")
    public ResponseEntity<Genre> getGenreById(@PathVariable ("id") Long id){
        Genre genre = genreService.getGenreById(id);
        return ResponseEntity.ok(genre);
    }

    @GetMapping("/by-title")
    public ResponseEntity<Genre> getGenreByTitle(@RequestParam ("title") String title){
        Genre genre = genreService.getGenreByTitle(title);
        return ResponseEntity.ok(genre);
    }

    @PostMapping()
    public ResponseEntity<Genre> createGenre(@RequestParam ("title") String title){
        Genre genre = genreService.createGenre(title);
        return ResponseEntity.ok(genre);
    }

    @PutMapping("/id")
    public ResponseEntity<Void> updateGenreById(@PathVariable ("id") Long id,
                                            @RequestParam ("title") String title){
        genreService.updateGenreById(id, title);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> deleteGenreById(@PathVariable ("id") Long id){
        genreService.deleteGenreById(id);
        return ResponseEntity.noContent().build();
    }
}
