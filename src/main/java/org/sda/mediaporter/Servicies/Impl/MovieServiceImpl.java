package org.sda.mediaporter.Servicies.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.json.JSONException;
import org.sda.mediaporter.Servicies.*;
import org.sda.mediaporter.api.OmdbApi;
import org.sda.mediaporter.api.TheMovieDb;
import org.sda.mediaporter.dtos.MovieUpdateDto;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.*;
import org.sda.mediaporter.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MovieServiceImpl implements MovieService {

    private final GenreService genreService;
    private final ContributorService contributorService;
    private final LanguageService languageService;
    private final MovieRepository movieRepository;
    private final FileService fileService;
    private final AudioService audioService;
    private final SubtitleService subtitleService;
    private final VideoService videoService;

    private Integer year;
    private Movie movie = new Movie();

    @Autowired
    public MovieServiceImpl(GenreService genreService, ContributorService contributorService, LanguageService languageService, MovieRepository movieRepository, FileService fileService, AudioService audioService, SubtitleService subtitleService, VideoService videoService) {
        this.genreService = genreService;
        this.contributorService = contributorService;
        this.languageService = languageService;
        this.movieRepository = movieRepository;
        this.fileService = fileService;
        this.audioService = audioService;
        this.subtitleService = subtitleService;
        this.videoService = videoService;
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

    @Override
    public Movie moveMovie(Long movieId, Path toPathWithoutFileName) {
        Movie movie = getMovieById(movieId);
        Path moviePath = Path.of(movie.getPath());
        Path destinationFullPath = toPathWithoutFileName.resolve(moviePath.getFileName());
        fileService.moveFile(moviePath, destinationFullPath);
        movie.setPath(destinationFullPath.toString());
        movieRepository.save(movie);
        return movie;
    }

    @Override
    public Movie updateMovie(Long movieId, MovieUpdateDto movieUpdateDto) {
        Movie movie = getMovieById(movieId);
        Path moviePath = Path.of(movie.getPath());
        fileService.renameFile(Path.of(movie.getPath()), movieUpdateDto.getTitle(), movieUpdateDto.getYear());
        movieRepository.delete(movie);
        Path renamedFullPath = fileService.renamedPath(moviePath, movieUpdateDto.getTitle(), movieUpdateDto.getYear());
        Movie updatedMovie = organizeMovieByPath(renamedFullPath);
        updatedMovie.setPath(renamedFullPath.toString());
        movieRepository.save(updatedMovie);
        return movie;
    }

    @Override
    public void copyMovie(Long movieId, Path toPathWithoutFileName) {
        Movie movie = getMovieById(movieId);
        Path moviePath = Path.of(movie.getPath());
        Path destinationPath = toPathWithoutFileName.resolve(moviePath.getFileName());
        fileService.copyFile(Path.of(movie.getPath()), destinationPath);
        try {
            Files.setLastModifiedTime(destinationPath, FileTime.from(Instant.now()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public List<Movie> organizedDownloadMovieFiles(Path moviesDownloadPath, Path destinationPath){
        List<Path> files = fileService.getVideoFiles(moviesDownloadPath);
        List<Movie> organizedMovies = new ArrayList<>();
        for (Path file : files){
            Movie movie = organizeMovieByPath(file);
            generateDownloadMovieFile(movie, destinationPath);
            movieRepository.save(movie);
            organizedMovies.add(movie);
        }
        return organizedMovies;
    }

    @Override
    public List<Movie> getMoviesFromPath(String path) {
        Path movieFilePath = Path.of(path);
        List<Path> videoFiles = fileService.getVideoFiles(movieFilePath);
        List<Movie> movies = new ArrayList<>();
        for (Path file : videoFiles) {
            if (checkMovie(title(file.getFileName().toString()))) {
                this.movie.setPath(file.toString());
                this.movie.setVideo(videoService.createVideoFromPath(file));
                this.movie.setAudios(audioService.createAudioListFromFile(file));
                this.movie.setSubtitles(subtitleService.createSubtitleListFromFile(file));

            }
            movieRepository.save(movie);
            movies.add(movie);
        } return movies;
    }



    private void generateDownloadMovieFile(Movie movie, Path destinationPath){
        Path moviePath = Path.of(movie.getPath());
        String moviePathExtension = fileService.getFileExtensionWithDot(moviePath);
        Path destinationFullPath = destinationPath.resolve(getGeneratedMovieFileName(movie) + moviePathExtension);
        fileService.moveFile(moviePath, destinationFullPath);
        movie.setPath(destinationFullPath.toString());
    }

    private String getTitle(String title, Integer year){
        TheMovieDb theMovieDb = new TheMovieDb(title, year);
        return theMovieDb.getTitle();
    }

    private String getGeneratedMovieFileName(Movie movie){
        return String.format("%s (%s)", movie.getTitle(), movie.getYear());
    }

    private String getGeneratedMovieSubFolder(Movie movie){
        return String.format("%s (%s)", movie.getTitle(), movie.getYear());
    }

    public Movie organizeMovieByPath(Path path) {
        if(checkMovie(title(path.getFileName().toString()))){
            return this.movie;
        }
        return null;
    }

    //try to find movie in api
    private boolean checkMovie(String[] titleElements) {
        for (int i = titleElements.length-1; i > 0; i--) {
            String[] subArray = Arrays.copyOfRange(titleElements, 0, i);
            try{
                String title = String.join(" ",subArray);
                System.out.println("title: " + title + " year: " + this.year);
                this.movie = getMovieFromApiByTitle(title, this.year);
                return true;
            }catch (JSONException ignored){

            }
        }
        return false;
    }

    //create filtered splitted filename without year, resolution. no title words
    private String[] title(String filename){
        this.year = getYearOfFileName(filename);
        List<String> titleElements = new ArrayList<>();
        for (int i = 0; i < splittedFilename(filename).length; i++) {
            String element = splittedFilename(filename)[i];
            if(!yearMatched(element) &&
                    !resolutionMatches(element) &&
                    !isStampedWords(element) &&
                    !isCodec(element) &&
                    !isLanguageCode(element) &&
                    !isExtension(element)
            ){
                titleElements.add(element);
            }
        }return titleElements.toArray(new String[0]);
    }

    //check if element is language code
    private boolean isLanguageCode(String element){
        for(LanguageCodes languageCode : LanguageCodes.values()){
            if(element.equalsIgnoreCase(languageCode.getIso6391())
                    || element.equalsIgnoreCase(languageCode.getIso6392T()) ||
            element.equalsIgnoreCase(languageCode.getIso6392B())){
                return true;
            }
        }return false;
    }

    //check if element is codec
    private boolean isCodec(String element){
        for(Codecs codec : Codecs.values()){
            if(element.equalsIgnoreCase(codec.getCodecName())){
                return true;
            }
        }return false;
    }

    //check if element is extension
    private boolean isExtension(String element){
        for(Extensions extension : Extensions.values()){
            if(element.equalsIgnoreCase(extension.getName()) && extension.getMediaTypes().equals(MediaTypes.VIDEO)){
                return true;
            }
        }return false;
    }

    //check if element is stamped word
    private boolean isStampedWords(String element){
        for(StampedWords stampedWords : StampedWords.values()){
            if(element.equalsIgnoreCase(stampedWords.getName())){
                return true;
            }
        }return false;
    }

    //find resolution
    private boolean resolutionMatches(String resolution){
        return resolution.toLowerCase().matches("^[0-9].*p$");
    }

    //find year
    private boolean yearMatched(String year){
        return year.matches("\\b\\d{4}\\b");
    }

    //replace special characters with space and split filename by space
    private String[] splittedFilename(String filename){
        return filename
                .replaceAll("\\[[^\\]]*\\]", "")
                .replaceAll("\\([^\\)]*\\)", "")
                .replaceAll("[!@#$%^&*()\\-_+=\\{\\}\\[\\]:;\"',.<>?/\\\\|+\\-*/%~^€©™®]", " ")
                .trim()
                .split("\\s+");
    }

    //get year of file name if exist
    private Integer getYearOfFileName(String filename){
        List<Integer> years = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(filename);
        while (matcher.find()) {
            years.add(Integer.parseInt(matcher.group()));
        }
        if (years.isEmpty()) {
            return null;
        }
        return years.getLast();
    }
}
