package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.GenreService;
import org.sda.mediaporter.dtos.GenreResponseDto;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    private Genre toEntity(Genre genre, GenreResponseDto genreResponseDto) {
        genre.setTitle(genreTitleValidated(genre, genreResponseDto.getTitle()));
        return genre;
    }

    private String genreTitleValidated(Genre genre, String title) {
        Optional<Genre> genreOptional = genreRepository.findGenreByTitle(title);
        if (genreOptional.isPresent() && !genre.getTitle().equals(title)) {
            throw new RuntimeException("Genre with title " + title + " already exists");
        }
        return title;
    }

    public Genre autoCreateGenre(String title) {
        Optional <Genre> genreOptional = genreRepository.findGenreByTitle(title);
        Genre genre = new Genre();
        genre.setTitle(title);
        return genreOptional.orElseGet(() -> genreRepository.save(genre));
    }
}
