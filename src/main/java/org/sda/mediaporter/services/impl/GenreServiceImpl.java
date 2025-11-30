package org.sda.mediaporter.services.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.services.GenreService;
import org.sda.mediaporter.dtos.GenreDto;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre autoCreateGenre(String title) {
        return title == null
                ? null
                : genreRepository.findGenreByTitle(title)
                .orElseGet(() -> genreRepository.save(
                        Genre.builder()
                                .title(capitalizeFirstLetter(title))
                                .build()));
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> getOrCreateGenresByTitles(List<String> genresTitles) {
        return genresTitles.stream()
                    .map(this::getOrCreateGenreByTitle).toList();

    }

    private Genre getOrCreateGenreByTitle(String genreTitle) {
        Optional<Genre> genreOptional = genreRepository.findGenreByTitle(genreTitle);
        return genreOptional.orElseGet(() -> genreRepository.save(Genre.builder()
                .title(genreTitle)
                .build()));
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
    public Genre getOrCreateGenre(String title) {
        return genreRepository.findGenreByTitle(title)
                .orElse(genreRepository.save(Genre.builder()
                                .title(title)
                        .build()));
    }

    @Override
    public Genre getGenreByTitleOrNull(String genreTitle) {
        return genreRepository.findGenreByTitle(genreTitle)
                .orElse(null);
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

    @Override
    public List<Genre> getGenresByTitles(List<String> genresTitles) {
        return genresTitles.stream()
                .map(this::getOrCreateGenre).toList();
    }

    private String validatedTitle(Genre genre, GenreDto genreDto){
        String titleDto = genreDto.getTitle();
        String title = genre.getTitle();

        Optional<Genre> genreOptional = genreRepository.findGenreByTitle(titleDto);
        if(titleDto == null){
            return title;
        }

        if(title != null && genre.getTitle().equals(titleDto)) {
                return title;
        }

        if (genreOptional.isEmpty()){
            return capitalizeFirstLetter(titleDto);
        }
        throw new EntityExistsException(String.format("Genre with title %s already exist", titleDto));
    }

    public static String capitalizeFirstLetter(String title) {
        String trimTitle = title.trim();

        return trimTitle.substring(0, 1).toUpperCase() + trimTitle.substring(1).toLowerCase();
    }
}
