package org.sda.mediaporter.services.movieServices.impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.api.TheMovieDbCreditsForMovieById;
import org.sda.mediaporter.api.TheMovieDbMovieById;
import org.sda.mediaporter.api.TheMovieDbMovieSearch;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieSearchDTO;
import org.sda.mediaporter.services.*;
import org.sda.mediaporter.dtos.MovieFilterDto;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Validated
public class MovieServiceImpl implements MovieService {

    private final GenreService genreService;
    private final VideoFilePathService videoFilePathService;
    private final LanguageService languageService;
    private final MovieRepository movieRepository;
    private final FileService fileService;
    private final CountryService countryService;
    private final ContributorService contributorService;

    @Autowired
    public MovieServiceImpl(GenreService genreService, VideoFilePathService videoFilePathService, LanguageService languageService, MovieRepository movieRepository, FileService fileService, CountryService countryService, ContributorService contributorService) {
        this.genreService = genreService;
        this.videoFilePathService = videoFilePathService;
        this.languageService = languageService;
        this.movieRepository = movieRepository;
        this.fileService = fileService;
        this.countryService = countryService;
        this.contributorService = contributorService;
    }

    @Override
    public Page<Movie> getMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public Page<Movie> getMoviesFromSourcePath(Pageable page, SourcePath sourcePath) {
        return movieRepository.findMoviesBySourcePath(page, sourcePath);
    }

    private void deleteMovieFromDbWithoutFile(){
        List<VideoFilePath> videoFilePaths = videoFilePathService.getAllVideoFilePaths();
        List<VideoFilePath> videoFilePathsWithInvalidPaths = videoFilePaths.stream()
                .filter(vfp -> !Files.exists(Path.of(vfp.getFilePath())))
                .toList();
        videoFilePathsWithInvalidPaths.forEach(videoFilePathService::deleteVideoFilePath);
        movieRepository.deleteMoviesWithoutVideoFilePaths();
    }

    @Override
    public List<Movie> getMoviesByTitleAndYear(String title, Integer year) {
        return movieRepository.findMovieByTitleAndYear(title, year);
    }

    @Override
    public Movie getMovieById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Movie with id %s not found", movieId)));
    }

    @Override
    public Movie getMovieByPath(String moviePath) {
        return movieRepository.findByPath(moviePath)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Movie with path %s not found", moviePath)));
    }

    @Override
    public Movie getMovieFromPathFile(Path moviePath) {
        String fileName = moviePath.getFileName().toString();
        return getMovieFromApi(fileName);
    }

    @Override
    public void deleteMovieById(Long id) {
        Movie movie = getMovieById(id);
        movie.getVideoFilePaths()
                .forEach(videoFilePath -> fileService.deleteFile(Path.of(videoFilePath.getFilePath())));
        movieRepository.delete(movie);
    }

    @Override
    public Page<Movie> getFiveLastAddedMovies(Pageable pageable) {
        return movieRepository.findLastFiveAddedMovies(pageable);
    }

    @Override
    public Movie createMovieFromPathFile(Path moviePath) {
        return null;
    }

    @Override
    public Page<Movie> getTopFiveMovies(Pageable pageable) {
        return movieRepository.findTopFiveMovies(pageable);
    }

    @Override
    public Page<Movie> filterMovies(Pageable page,
                                    MovieFilterDto movieFilterDto) {
        List<Long> genres = movieFilterDto.getGenreIds();
        if(genres == null){
            genres = genreService.getAllGenres().stream().map(Genre::getId).toList();
        }

        List<Long> countries = movieFilterDto.getCountryIds();
        if(countries == null){
            countries = countryService.getAllCountries().stream().map(Country::getId).toList();
        }

        List<Long> audioLanguages = movieFilterDto.getALanguageIds();

        return movieRepository.filterMovies(
                page,
                movieFilterDto.getTitle(),
                movieFilterDto.getYear(),
                movieFilterDto.getRating(),
                genres,
                countries,
                audioLanguages
                );
    }

    @Override
    public Page<Movie> filterByAudioLanguage(Pageable page, List<Long> aLanguageIds) {
        return movieRepository.filterByAudioLanguage(page, aLanguageIds);
    }

    //try to find movie in api
    private Movie getMovieFromApi(String fileTitle) {

        String filteredFileTitle = fileService.getSafeFileName(fileTitle); //remove all chars for creating a path
        Integer yearOfFileTitle = findLastYearFromTitle(filteredFileTitle);

        TheMovieDbMovieSearchDTO movieAPISearchDTO = movieAPISearchDTO(filteredFileTitle, yearOfFileTitle);
        if(movieAPISearchDTO != null){
            Long theMovieDbMovieId = movieAPISearchDTO.getTheMovieDbId();
            TheMovieDbMovieById theMovieDbMovieById = new TheMovieDbMovieById(theMovieDbMovieId);
            TheMovieDbCreditsForMovieById theMovieDbCreditsForMovieById = new TheMovieDbCreditsForMovieById(theMovieDbMovieId);

            List<Contributor> writers = contributorService.getCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForMovieById.getWriters());
            List<Contributor> actors = contributorService.getCastsByTheMovieDbCastsDto(theMovieDbCreditsForMovieById.getActors());
            List<Contributor> directors = contributorService.getCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForMovieById.getDirectors());
            List<Genre> genres = genreService.getGenresByTitles(theMovieDbMovieById.getTheMovieDbMovieDto().getGenres());
            Language originalLanguage = getLanguageByCodeOrNull(theMovieDbMovieById.getTheMovieDbMovieDto().getLanguageCode());
            List<Country> countries = getCountriesByCodes(theMovieDbMovieById.getTheMovieDbMovieDto().getCountries());
            return toEntity(theMovieDbMovieById.getTheMovieDbMovieDto(), writers, actors, directors, genres, originalLanguage, countries);
        }

        return new Movie();
    }

    private Movie getOrCreateMovieFromApi(String fileTitle) {

        String filteredFileTitle = fileService.getSafeFileName(fileTitle); //remove all chars for creating a path
        Integer yearOfFileTitle = findLastYearFromTitle(filteredFileTitle);

        TheMovieDbMovieSearchDTO movieAPISearchDTO = movieAPISearchDTO(filteredFileTitle, yearOfFileTitle);
        if(movieAPISearchDTO != null){
            Optional<Movie> movieOptional = movieRepository.findMovieByTheMovieDbId(movieAPISearchDTO.getTheMovieDbId());
            if(movieOptional.isPresent()){
                return movieOptional.get();
            }else {
                Long theMovieDbMovieId = movieAPISearchDTO.getTheMovieDbId();
                TheMovieDbMovieById theMovieDbMovieById = new TheMovieDbMovieById(theMovieDbMovieId);
                TheMovieDbCreditsForMovieById theMovieDbCreditsForMovieById = new TheMovieDbCreditsForMovieById(theMovieDbMovieId);

                List<Contributor> writers = contributorService.getOrCreateCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForMovieById.getWriters());
                List<Contributor> actors = contributorService.getOrCreateCastsByTheMovieDbCastsDto(theMovieDbCreditsForMovieById.getActors());
                List<Contributor> directors = contributorService.getOrCreateCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForMovieById.getDirectors());
                List<Genre> genres = genreService.getOrCreateGenresByTitles(theMovieDbMovieById.getTheMovieDbMovieDto().getGenres());
                Language originalLanguage = getLanguageByCodeOrNull(theMovieDbMovieById.getTheMovieDbMovieDto().getLanguageCode());
                List<Country> countries = getCountriesByCodes(theMovieDbMovieById.getTheMovieDbMovieDto().getCountries());
                return movieRepository.save(toEntity(theMovieDbMovieById.getTheMovieDbMovieDto(), writers, actors, directors, genres, originalLanguage, countries));
            }
        }

        return new Movie();
    }

    private List<Country> getCountriesByCodes(List<String> countryCodes){
        return countryCodes.stream()
                .map(countryService::getCountryByCode).toList();
    }

    private Language getLanguageByCodeOrNull(String languageCode){
        try{
            return languageService.getLanguageByCode(languageCode);
        }catch (EntityNotFoundException e){
            return null;
        }
    }

    private Movie toEntity(TheMovieDbMovieDto theMovieDbMovieDto, List<Contributor> writers, List<Contributor> actors, List<Contributor> directors, List<Genre> genres, Language originalLanguage, List<Country> countries){
        return Movie.builder()
                .title(theMovieDbMovieDto.getTitle())
                .originalTitle(theMovieDbMovieDto.getOriginalTitle())
                .theMovieDbId(theMovieDbMovieDto.getTheMoveDbId())
                .overview(theMovieDbMovieDto.getOverview())
                .year(theMovieDbMovieDto.getYear())
                .rate(theMovieDbMovieDto.getRate())
                .writers(writers)
                .actors(actors)
                .directors(directors)
                .genres(genres)
                .originalLanguage(originalLanguage)
                .countries(countries)
                .poster(theMovieDbMovieDto.getPoster())
                .releaseDate(theMovieDbMovieDto.getReleaseDate())
                .build();
    }

    private TheMovieDbMovieSearchDTO movieAPISearchDTO(String fileTitle, Integer year){
        String filteredFileTitle = removeAllCharsWithLastYearAndAfter(fileTitle, year);
        String[] splitedFileTitle = filteredFileTitle.split(" ");
        int splitedFileTitleLength = splitedFileTitle.length;
        for (int i = 0 ; i < splitedFileTitleLength; i++) {
            String[] fileTitleElements = Arrays.copyOf(splitedFileTitle, splitedFileTitleLength-i);
            String newFileTitle = String.join(" ", fileTitleElements);

            TheMovieDbMovieSearch theMovieDbMovieSearch = new TheMovieDbMovieSearch(newFileTitle, year);
            List<TheMovieDbMovieSearchDTO> movieAPISearchDTOs = theMovieDbMovieSearch.getMoviesSearchFromApi();
            int movieSearchSize = theMovieDbMovieSearch.getMoviesSearchFromApi().size();
            if(movieSearchSize == 1){
                return theMovieDbMovieSearch.getMoviesSearchFromApi().getFirst();
            }else if(movieSearchSize > 1) {
                int titleWords = fileTitleElements.length;
                List<TheMovieDbMovieSearchDTO> searchResult = movieAPISearchDTOs.stream()
                        .filter(m -> fileService.getSafeFileName(m.getTitle()).split(" ").length == titleWords).toList();
                if (searchResult.size() == 1) {
                    return searchResult.getFirst();
                }
            }
        }return null;
    }
    

    private Integer findLastYearFromTitle(String fileTitle){
        Pattern pattern = Pattern.compile("\\b(\\d{4})\\b");
        Matcher matcher = pattern.matcher(fileTitle);
        String lastYear = null;
        while (matcher.find()) {
            lastYear = matcher.group(1); // keep updating to get the last match
        }
        return lastYear == null
                ? null
                : Integer.parseInt(lastYear);
    }

    private String removeAllCharsWithLastYearAndAfter(String fileTitle, Integer year) {
        if(year != null){
            String yearStr = String.valueOf(year);
            int lastIndex = fileTitle.lastIndexOf(yearStr);
            if (lastIndex == -1) {
                return fileTitle;
            }

            return fileTitle.substring(0, lastIndex).trim().replaceAll(" ", "+");

        }
        return fileTitle;
    }
}
