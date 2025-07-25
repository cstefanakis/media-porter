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

    public Genre autoCreateGenre(String title) {
        Optional <Genre> genreOptional = genreRepository.findGenreByTitle(title);
        if(genreOptional.isPresent()){
            return genreOptional.get();
        }
        return genreRepository.save(Genre.builder()
                .title(capitalizeFirstLetter(title))
                .build());
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
        return genreRepository.save(Genre.builder()
                .title(validatedTitle(new Genre(), title))
                .build());
    }

    @Override
    public void updateGenreById(Long id, String title) {
        Genre genre = getGenreById(id);
        genre.setTitle(validatedTitle(genre, title));
        genreRepository.save(genre);
    }

    @Override
    public void deleteGenreById(Long id) {
        Genre genre = getGenreById(id);
        genreRepository.delete(genre);
    }

    private String validatedTitle(Genre genre, String title){
        Optional<Genre> genreOptional = genreRepository.findGenreByTitle(title);
        if(genre.getTitle() != null && genre.getTitle().equals(title)) {
                return genre.getTitle();
            }
        title = capitalizeFirstLetter(title);
        if (genreOptional.isEmpty()){
            return title;
        }
        throw new EntityExistsException(String.format("Genre with title %s already exist", title));
    }

    public static String capitalizeFirstLetter(String title) {
        String trimTitle = title.trim();
        return trimTitle.substring(0, 1).toUpperCase() + trimTitle.substring(1).toLowerCase();
    }
}
