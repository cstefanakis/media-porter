package org.sda.mediaporter.controllers;

import jakarta.validation.Valid;
import org.sda.mediaporter.Services.GenreService;
import org.sda.mediaporter.dtos.GenreDto;
import org.sda.mediaporter.models.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<Genre>> getAllGenres(){
        List<Genre> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable ("id") Long id){
        Genre genre = genreService.getGenreById(id);
        return ResponseEntity.ok(genre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-title")
    public ResponseEntity<Genre> getGenreByTitle(@RequestParam ("title") String title){
        Genre genre = genreService.getGenreByTitle(title);
        return ResponseEntity.ok(genre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Genre> createGenre(@RequestBody @Valid GenreDto genreDto){
        Genre genre = genreService.createGenre(genreDto);
        return ResponseEntity.ok(genre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGenreById(@PathVariable ("id") Long id,
                                                @RequestBody GenreDto genreDto){
        genreService.updateGenreById(id, genreDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenreById(@PathVariable ("id") Long id){
        genreService.deleteGenreById(id);
        return ResponseEntity.noContent().build();
    }
}
