package org.sda.mediaporter.services.tvShowServices.impl;

import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowDto;
import org.sda.mediaporter.models.Country;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.services.*;
import org.sda.mediaporter.api.TheMovieDbTvShowSearch;
import org.sda.mediaporter.api.TheMovieDbTvShowsById;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowSearchDTO;
import org.sda.mediaporter.models.TvShow;
import org.sda.mediaporter.repositories.*;
import org.sda.mediaporter.services.tvShowServices.TvShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TvShowServiceImpl implements TvShowService {
    private final GenreService genreService;
    private final CountryService countryService;
    private final TvShowRepository tvShowRepository;

    @Autowired
    public TvShowServiceImpl(GenreService genreService, CountryService countryService, TvShowRepository tvShowRepository) {
        this.genreService = genreService;
        this.countryService = countryService;
        this.tvShowRepository = tvShowRepository;
    }

    @Override
    public TvShow updateTvShowModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime) {
        tvShow.setLastModificationDateTime(modificationDateTime);
        return tvShowRepository.save(tvShow);
    }

    @Override
    public TvShow getOrCreateTvShowByTitle(String tvShowTitle) {

        TheMovieDbTvShowSearchDTO tvShowFromSearch = getTvShowAPISearchDTO(tvShowTitle);
        assert tvShowFromSearch != null;
        Long tvShowId = tvShowFromSearch.getTheMovieDbId();
        Optional<TvShow> tvShowOptional = tvShowRepository.findTvShowByTheMovieDBId(tvShowId);
        if(tvShowOptional.isPresent()){
            return tvShowOptional.get();
        }else{
            TheMovieDbTvShowsById tvShow = new TheMovieDbTvShowsById(tvShowId);
            List<Country> countries = countryService.getCountriesByCodes(tvShow.getTheMovieDbTvShowDto().getCountriesCodes());
            List<Genre> genres = genreService.getGenresByTitles(tvShow.getTheMovieDbTvShowDto().getGenres());
            return theMovieDbTvShowDtoToEntity(tvShow.getTheMovieDbTvShowDto(), countries, genres);
        }
    }

    private TvShow theMovieDbTvShowDtoToEntity(TheMovieDbTvShowDto theMovieDbTvShowDto, List<Country> countries, List<Genre> genres){
        return TvShow.builder()
                .title(theMovieDbTvShowDto.getTitle())
                .originalTitle(theMovieDbTvShowDto.getOriginalTitle())
                .year(theMovieDbTvShowDto.getYear())
                .firstAirDate(theMovieDbTvShowDto.getFirstAirDate())
                .lastAirDate(theMovieDbTvShowDto.getLastAirDate())
                .overview(theMovieDbTvShowDto.getOverview())
                .homePage(theMovieDbTvShowDto.getHomePage())
                .poster(theMovieDbTvShowDto.getPoster())
                .rate(theMovieDbTvShowDto.getRate())
                .status(theMovieDbTvShowDto.getStatus())
                .theMoveDBTvShowId(theMovieDbTvShowDto.getTheMovieDbId())
                .countries(countries)
                .genres(genres)
                .build();
    }

    private TheMovieDbTvShowSearchDTO getTvShowAPISearchDTO(String tvShowTitle){

        //search from API for TVShow
        TheMovieDbTvShowSearch tvShowsFromAPISearch = new TheMovieDbTvShowSearch(tvShowTitle);

        String[] tvShowTitleWords = tvShowTitle.split(" ");
        int lastElementOfTvShowTitleWords = tvShowTitleWords.length-1;
        String lastWordOfTitle = tvShowTitleWords[lastElementOfTvShowTitleWords];
        String yearRegex = "(19[5-9]\\d|20[0-3]\\d)";

        if(tvShowsFromAPISearch.getTvShows().isEmpty() && lastWordOfTitle.matches(yearRegex)){
            int lastIndexOfTitle =  tvShowTitle.lastIndexOf(lastWordOfTitle);
            tvShowTitle = tvShowTitle.substring(0, lastIndexOfTitle).trim();
            tvShowsFromAPISearch = new TheMovieDbTvShowSearch(tvShowTitle);
        }

        List<TheMovieDbTvShowSearchDTO> tvShowsFromAPI = tvShowsFromAPISearch.getTvShows();
        if(tvShowsFromAPI.size() == 1){
            return tvShowsFromAPI.getFirst();
        }else if (tvShowsFromAPI.size() > 1) {
            String finalTvShowTitle = tvShowTitle;
            List<TheMovieDbTvShowSearchDTO> filtered = tvShowsFromAPI.stream()
                    .filter(ts -> getLengthOfTitle(ts.getTitle()) == getLengthOfTitle(finalTvShowTitle))
                    .toList();
            if (filtered.size() == 1) {
                return filtered.getFirst();
            }
        }
       return null;
    }

    private int getLengthOfTitle(String tvShowTitle) {
        if (tvShowTitle == null || tvShowTitle.isBlank()) {
            return 0;
        }

        String cleaned = tvShowTitle
                .replaceAll("[!@#$%^&*()_+={}\\[\\]|?><\"':;\\/?\\-]", " ")
                .trim();

        return cleaned.isEmpty() ? 0 : cleaned.split("\\s+").length;
    }
}
