package org.sda.mediaporter.Servicies.Impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final GenreService genreService;
    private final ContributorService contributorService;
    private final LanguageService languageService;

    @Autowired
    public MovieServiceImpl(GenreService genreService, ContributorService contributorService, LanguageService languageService) {
        this.genreService = genreService;
        this.contributorService = contributorService;
        this.languageService = languageService;
    }

    @Override
    public Movie getMovieByTitle(String title, Integer year) {
        try{
            return omdbApiToEntity(new Movie(), title, year);
        }catch (JSONException e){
            System.out.println("JSONException: "+e.getMessage());
            return theMovieDbToEntity(new Movie(), title, year);
        }
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
