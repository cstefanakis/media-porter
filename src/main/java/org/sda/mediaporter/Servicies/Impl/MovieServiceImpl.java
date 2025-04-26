package org.sda.mediaporter.Servicies.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.json.JSONException;
import org.sda.mediaporter.Servicies.ContributorService;
import org.sda.mediaporter.Servicies.GenreService;
import org.sda.mediaporter.Servicies.LanguageService;
import org.sda.mediaporter.Servicies.MovieService;
import org.sda.mediaporter.api.OmdbApi;
import org.sda.mediaporter.api.TheMovieDb;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final GenreService genreService;
    private final ContributorService contributorService;
    private final LanguageService languageService;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(GenreService genreService, ContributorService contributorService, LanguageService languageService, MovieRepository movieRepository) {
        this.genreService = genreService;
        this.contributorService = contributorService;
        this.languageService = languageService;
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie getMovieFromApiByTitle(String title, Integer year) {
        try{
            return omdbApiToEntity(new Movie(), title, year);
        }catch (JSONException e){
            System.out.println("JSONException: "+e.getMessage());
            return theMovieDbToEntity(new Movie(), title, year);
        }
    }

    @Override
    public Movie getMovieById(Long movieId) {
        Optional<Movie> movieOptional =  movieRepository.findById(movieId);
        if (movieOptional.isPresent()){
            if(isFileExists(movieOptional.get().getPath())){
                return movieOptional.get();
            }deleteFile(movieOptional.get().getPath());
        }throw new EntityNotFoundException(String.format("Movie with id %s not found", movieId));
    }

    @Override
    public Movie getMovieByPath(String moviePath) {
        return movieRepository.findByPath(moviePath).orElseThrow(() -> new EntityNotFoundException(String.format("Movie with path %s not found", moviePath)));

    }

    @Override
    public void deleteMovieById(Long id) {
        Movie movie = getMovieById(id);
        deleteFile(movie.getPath());
        movieRepository.delete(movie);
    }

    private void deleteFile(String pathStr){
        Path path = Path.of(pathStr);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot delete file: %s", pathStr));
        }
    }

    private boolean isFileExists(String pathStr){
        Path path = Path.of(pathStr);
        return Files.exists(path);
    }

    private Movie omdbApiToEntity(Movie movie, String title, Integer year){
        OmdbApi omdbApi = new OmdbApi(getTitle(title, year), year);
        TheMovieDb theMovieDb = new TheMovieDb(title, year);
        movie.setTitle(omdbApi.getTitle());
        movie.setOriginalTitle(theMovieDb.getOriginalTitle());
        movie.setYear(omdbApi.getYear());
        movie.setRating(omdbApi.getImdbRating());
        movie.setReleaseDate(omdbApi.getReleasedDate());
        movie.setGenres(getGenres(omdbApi.getGenre()));
        movie.setDirectors(getContributors(omdbApi.getDirector()));
        movie.setWriters(getContributors(omdbApi.getWriter()));
        movie.setActors(getContributors(omdbApi.getActors()));
        movie.setPlot(omdbApi.getPlot());
        movie.setCountry(omdbApi.getCountry());
        movie.setPoster(omdbApi.getPoster());
        movie.setLanguages(getLanguagesByTitle(omdbApi.getLanguages()));
        return movie;
    }

    private List<Genre> getGenres(List<String> apiGenres) {
        List<Genre> genres = new ArrayList<>();
        for (String genre : apiGenres){
            genres.add(genreService.autoCreateGenre(genre));
        }
        return genres;
    }

    private List<Contributor> getContributors(List<String> apiContributors) {
        List<Contributor> contributors = new ArrayList<>();
        for (String contributor : apiContributors){
            contributors.add(contributorService.autoCreateContributor(contributor));
        }
        return contributors;
    }

    private List<Language> getLanguagesByTitle(List<String> apiLanguages) {
        List<Language> languages = new ArrayList<>();
        for (String language : apiLanguages){
            languages.add(languageService.autoCreateLanguageByTitle(language));
        }
        return languages;
    }

    private List<Language> getLanguagesByCode(List<String> apiLanguages) {
        return apiLanguages.stream().map(languageService::autoCreateLanguageByCode).toList();
    }

    private Movie theMovieDbToEntity(Movie movie, String title, Integer year){
        TheMovieDb theMovieDb = new TheMovieDb(title, year);
        movie.setTitle(theMovieDb.getTitle());
        movie.setOriginalTitle(theMovieDb.getOriginalTitle());
        movie.setYear(theMovieDb.getYear());
        movie.setRating(theMovieDb.getRating());
        movie.setReleaseDate(theMovieDb.getReleaseDate());
        movie.setGenres(getGenres(theMovieDb.getGenres()));
        movie.setPlot(theMovieDb.getOverview());
        movie.setCountry(null);
        movie.setPoster(theMovieDb.getPoster());
        movie.setLanguages(getLanguagesByCode(theMovieDb.getLanguages()));
        return movie;
    }

    private String getTitle(String title, Integer year){
        TheMovieDb theMovieDb = new TheMovieDb(title, year);
        return theMovieDb.getTitle();
    }
}
