package org.sda.mediaporter.services.movieServices.impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.api.TheMovieDbCreditsForMovieById;
import org.sda.mediaporter.api.TheMovieDbMovieById;
import org.sda.mediaporter.api.TheMovieDbMovieSearch;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieSearchDTO;
import org.sda.mediaporter.models.metadata.Character;
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

import java.nio.file.Path;
import java.time.LocalDateTime;
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
    private final CharacterService characterService;

    @Autowired
    public MovieServiceImpl(GenreService genreService, VideoFilePathService videoFilePathService, LanguageService languageService, MovieRepository movieRepository, FileService fileService, CountryService countryService, ContributorService contributorService, CharacterService characterService) {
        this.genreService = genreService;
        this.videoFilePathService = videoFilePathService;
        this.languageService = languageService;
        this.movieRepository = movieRepository;
        this.fileService = fileService;
        this.countryService = countryService;
        this.contributorService = contributorService;
        this.characterService = characterService;
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
        for(VideoFilePath videoFilePath : videoFilePaths){
            String filePath = videoFilePath.getFilePath();
            Movie movie = getMovieById(videoFilePath.getMovie().getId());
            if(!fileService.isFilePathExist(filePath)){
                videoFilePathService.deleteVideoFilePath(videoFilePath, movie, null);
            }
        }
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
                .orElse(null);
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
                .forEach(videoFilePath ->
                {
                    Path fullPath = videoFilePathService.getFullPathFromVideoFilePath(videoFilePath);
                    fileService.deleteFile(fullPath);
                    fileService.deleteSubDirectories(fullPath);
                });
        movieRepository.delete(movie);
    }

    @Override
    public Page<Movie> getFiveLastAddedMovies(Pageable pageable) {
        return movieRepository.findLastFiveAddedMovies(pageable);
    }

    @Override
    public Movie getOrCreateMovieFromPathFile(Path moviePath) {
        String movieFileName = moviePath.getFileName().toString();
        return getOrCreateMovieFromApi(movieFileName);
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

    @Override
    public void updateModificationDateTime(Movie movie, Path filePath) {
        LocalDateTime modificationDateTime = fileService.getModificationLocalDateTimeOfPath(filePath);
        movie.setLastModificationDateTime(modificationDateTime);
        movieRepository.save(movie);
    }

    //try to find movie in api
    private Movie getMovieFromApi(String fileTitle) {

        String filteredFileTitle = fileService.getSafeFileName(fileTitle); //remove all chars for creating a path
        Integer yearOfFileTitle = findLastYearFromTitle(filteredFileTitle);

        TheMovieDbMovieSearchDTO movieAPISearchDTO = getMovieAPISearchDTO(filteredFileTitle, yearOfFileTitle);
        if(movieAPISearchDTO != null){
            Long theMovieDbMovieId = movieAPISearchDTO.getTheMovieDbId();
            TheMovieDbMovieById theMovieDbMovieById = new TheMovieDbMovieById(theMovieDbMovieId);
            TheMovieDbCreditsForMovieById theMovieDbCreditsForMovieById = new TheMovieDbCreditsForMovieById(theMovieDbMovieId);
            List<Contributor> writers = contributorService.getCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForMovieById.getWriters());
            List<Contributor> actors = contributorService.getCastsByTheMovieDbCastsDto(theMovieDbCreditsForMovieById.getActors());
            List<Contributor> directors = contributorService.getCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForMovieById.getDirectors());
            List<Genre> genres = genreService.getGenresByTitles(theMovieDbMovieById.getTheMovieDbMovieDto().getGenres());
            Language originalLanguage = languageService.getLanguageByCodeOrNull(theMovieDbMovieById.getTheMovieDbMovieDto().getLanguageCode());
            List<Country> countries = getCountriesByCodes(theMovieDbMovieById.getTheMovieDbMovieDto().getCountries());
            return toEntity(theMovieDbMovieById.getTheMovieDbMovieDto(), writers, actors, directors, genres, originalLanguage, countries);
        }

        return new Movie();
    }


    private Movie getOrCreateMovieFromApi(String fileTitle) {
        String filteredFileTitle = fileTitle.replace(fileService.getFileExtensionWithDot(fileTitle), "");
        filteredFileTitle = fileService.getSafeFileName(filteredFileTitle); //remove all chars for creating a path
        Integer yearOfFileTitle = findLastYearFromTitle(filteredFileTitle);
        filteredFileTitle = getTitleBeforeLastYearInTitle(filteredFileTitle, yearOfFileTitle);


        TheMovieDbMovieSearchDTO movieAPISearchDTO = getMovieAPISearchDTO(filteredFileTitle, yearOfFileTitle);
        if(movieAPISearchDTO != null){
            Long theMovieDbMovieId = movieAPISearchDTO.getTheMovieDbId();
            Optional<Movie> movieOptional = movieRepository.findMovieByTheMovieDbId(theMovieDbMovieId);
            if(movieOptional.isPresent()){
                return movieOptional.get();
            }else {
                TheMovieDbMovieById theMovieDbMovieById = new TheMovieDbMovieById(theMovieDbMovieId);
                TheMovieDbCreditsForMovieById theMovieDbCreditsForMovieById = new TheMovieDbCreditsForMovieById(theMovieDbMovieId);
                List<Contributor> writers = contributorService.getOrCreateCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForMovieById.getWriters());
                List<Contributor> actors = contributorService.getOrCreateCastsByTheMovieDbCastsDto(theMovieDbCreditsForMovieById.getActors());
                List<Contributor> directors = contributorService.getOrCreateCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForMovieById.getDirectors());
                List<Genre> genres = genreService.getOrCreateGenresByTitles(theMovieDbMovieById.getTheMovieDbMovieDto().getGenres());
                Language originalLanguage = languageService.getLanguageByCodeOrNull(theMovieDbMovieById.getTheMovieDbMovieDto().getLanguageCode());
                List<Country> countries = getCountriesByCodes(theMovieDbMovieById.getTheMovieDbMovieDto().getCountries());
                Movie movie = movieRepository.save(toEntity(theMovieDbMovieById.getTheMovieDbMovieDto(), writers, actors, directors, genres, originalLanguage, countries));
                List<Character> characters = characterService.createCharactersForMovie(theMovieDbCreditsForMovieById.getActors(), movie);
                movie.setCharacters(characters);
                return movie;
            }
        }

        return null;
    }

    private List<Country> getCountriesByCodes(List<String> countryCodes){
        return countryCodes.stream()
                .map(countryService::getCountryByCodeOrNull)
                .filter(Objects::isNull)
                .toList();
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

    private TheMovieDbMovieSearchDTO getMovieAPISearchDTO(String searchTitle, Integer year){

        String[] searchTitleWords = searchTitle.split(" ");
        int fileTitleWordsLength = searchTitleWords.length;
        for (int i = 0 ; i < fileTitleWordsLength; i++) {
            String[] fileTitleWordsWithoutLastWord = Arrays.copyOf(searchTitleWords, fileTitleWordsLength-i);
            String newSearchTitle = String.join(" ", fileTitleWordsWithoutLastWord).replaceAll(" ", "+");

            TheMovieDbMovieSearch theMovieDbMovieSearch = new TheMovieDbMovieSearch(newSearchTitle, year);
            List<TheMovieDbMovieSearchDTO> movieAPISearchDTOs = theMovieDbMovieSearch.getMoviesSearchFromApi();

            int movieSearchSize = movieAPISearchDTOs.size();

            if(movieSearchSize == 1){
                return movieAPISearchDTOs.getFirst();
            }
            if(movieSearchSize > 1) {
                TheMovieDbMovieSearchDTO filteredMovieDto = filteredMovieDto(newSearchTitle, movieAPISearchDTOs);
                if(filteredMovieDto != null){
                    return filteredMovieDto;
                }
            }
        }return null;
    }

    private TheMovieDbMovieSearchDTO filteredMovieDto(String searchTitle, List<TheMovieDbMovieSearchDTO> listOfMovies){
        for(TheMovieDbMovieSearchDTO theMovieDbMovieDto : listOfMovies){
            String searchTitleWithoutSymbols = fileService.getSafeFileName(searchTitle).toLowerCase();
            String originalTitleWithoutSymbols = fileService.getStringWithoutDiacritics(theMovieDbMovieDto.getOriginalTitle());
            originalTitleWithoutSymbols = fileService.getSafeFileName(originalTitleWithoutSymbols).toLowerCase();
            String titleWithoutSymbols = fileService.getStringWithoutDiacritics(theMovieDbMovieDto.getTitle());
            titleWithoutSymbols = fileService.getSafeFileName(titleWithoutSymbols).toLowerCase();
            if(isTheSameSearchTitleWithOriginalTitleOrTitle(searchTitleWithoutSymbols, originalTitleWithoutSymbols, titleWithoutSymbols)){
                return theMovieDbMovieDto;
            }
            if(isTheSearchTitleLengthSameWithLengthOfTitle(searchTitleWithoutSymbols, originalTitleWithoutSymbols, titleWithoutSymbols)){
                return theMovieDbMovieDto;
            }
        }
        return null;
    }

    private boolean isTheSameSearchTitleWithOriginalTitleOrTitle(String searchTitle, String originalTitle, String title){
        return searchTitle.equals(originalTitle) || searchTitle.equals(title);
    }

    private boolean isTheSearchTitleLengthSameWithLengthOfTitle(String searchTitle, String originalTitle, String title){
        int searchTitleWords = searchTitle.split(" ").length;
        int originalTitleWords = originalTitle.split(" ").length;
        int titleWords = title.split(" ").length;
        return searchTitleWords == originalTitleWords || searchTitleWords == titleWords;
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

    private String getTitleBeforeLastYearInTitle(String fileTitle, Integer year) {
        if(year != null){
            String yearStr = String.valueOf(year);
            int lastIndex = fileTitle.lastIndexOf(yearStr);
            if (lastIndex == -1) {
                return fileTitle;
            }

            return fileTitle.substring(0, lastIndex).trim();

        }
        return fileTitle;
    }
}
