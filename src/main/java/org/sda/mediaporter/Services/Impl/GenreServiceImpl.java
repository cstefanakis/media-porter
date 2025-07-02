package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.GenreService;
import org.sda.mediaporter.dtos.GenreResponseDto;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Genre with id %s not found", id)));
    }

    @Override
    public Genre getGenreByTitle(String title) {
        return genreRepository.findGenreByTitle(title).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Genre with title %s not found",title)));
    }

    @Override
    public Genre createGenre(String title) {
        if(genreRepository.findGenreByTitle(title).isEmpty()) {
            return genreRepository.save(Genre.builder()
                    .title(title)
                    .build());
        }throw new EntityExistsException("Genre with title %s already exist");
    }

    @Override
    public void updateGenreById(Long id, String title) {
        if(genreRepository.findGenreByTitle(title).isEmpty()) {
            getGenreById(id).setTitle(title);
        }throw new EntityExistsException(String.format("Genre with title %s already exist"));
    }

    @Override
    public void deleteGenreById(Long id) {
        Genre genre = getGenreById(id);
        genreRepository.delete(genre);
    }
}
