package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.GenreService;
import org.sda.mediaporter.dtos.GenreDto;
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
        return genreOptional.orElseGet(() -> genreRepository.save(Genre.builder()
                .title(capitalizeFirstLetter(title))
                .build()));
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
    public Genre createGenre(GenreDto genreDto) {
        return genreRepository.save(toEntity(new Genre(), genreDto));
    }

    @Override
    public void updateGenreById(Long id, GenreDto genreDto) {
        Genre genre = getGenreById(id);
        genreRepository.save(toEntity(genre, genreDto));
    }

    private Genre toEntity(Genre genre, GenreDto genreDto){

        genre.setTitle(validatedTitle(genre , genreDto));

        return genre;
    }

    @Override
    public void deleteGenreById(Long id) {
        Genre genre = getGenreById(id);
        genreRepository.delete(genre);
    }

    private String validatedTitle(Genre genre, GenreDto genreDto){
        String titleDto = genreDto.getTitle();
        String title = genre.getTitle();

        Optional<Genre> genreOptional = genreRepository.findGenreByTitle(titleDto);
        if(title != null && genre.getTitle().equals(titleDto)) {
                return title;
            }
        titleDto = capitalizeFirstLetter(titleDto);
        if (genreOptional.isEmpty()){
            return titleDto;
        }
        throw new EntityExistsException(String.format("Genre with title %s already exist", titleDto));
    }

    public static String capitalizeFirstLetter(String title) {
        String trimTitle = title.trim();
        return trimTitle.substring(0, 1).toUpperCase() + trimTitle.substring(1).toLowerCase();
    }
}
