package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.json.JSONException;
import org.sda.mediaporter.Services.*;
import org.sda.mediaporter.api.OmdbApi;
import org.sda.mediaporter.api.TheMovieDb;
import org.sda.mediaporter.dtos.MovieFilterDto;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.*;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final VideoService videoService;
    private final SubtitleService subtitleService;
    private final AudioRepository audioRepository;
    private final VideoRepository videoRepository;
    private final SubtitleRepository subtitleRepository;

    private Integer year;

    @Autowired
    public MovieServiceImpl(GenreService genreService, ContributorService contributorService, LanguageService languageService, MovieRepository movieRepository, FileService fileService, AudioService audioService, VideoService videoService, SubtitleService subtitleService, AudioRepository audioRepository, VideoRepository videoRepository, SubtitleRepository subtitleRepository) {
        this.genreService = genreService;
        this.contributorService = contributorService;
        this.languageService = languageService;
        this.movieRepository = movieRepository;
        this.fileService = fileService;
        this.audioService = audioService;
        this.videoService = videoService;
        this.subtitleService = subtitleService;
        this.audioRepository = audioRepository;
        this.videoRepository = videoRepository;
        this.subtitleRepository = subtitleRepository;
    }

    @Override
    public Page<Movie> getMovies(Pageable pageable) {
        Page<Movie> movies =  movieRepository.findAll(pageable);
        for (Movie movie : movies) {
            if(!Files.exists(Path.of(movie.getPath()))){
                movieRepository.delete(movie);
            }
        }return movies;
    }

    @Override
    public Movie getMovieFromApiByTitle(Movie movie, String title, Integer year) {
        return omdbApiToEntity(movie, title, year);
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
        String movieTitleWithYear = getGeneratedMovieSubFolder(movie.getTitle(), movie.getYear());
        fileService.deleteFile(Path.of(movie.getPath()), movieTitleWithYear);
        movieRepository.delete(movie);
    }

    @Override
    public Movie moveMovie(Long movieId, Path toPathWithoutFileName) {
        Movie movie = getMovieById(movieId);
        Path moviePath = Path.of(movie.getPath());
        Path destinationFullPath = toPathWithoutFileName.resolve(moviePath.getFileName());
        fileService.moveFile(moviePath, getGeneratedMovieSubFolder(movie.getTitle(), movie.getYear()),destinationFullPath);
        movie.setPath(destinationFullPath.toString());
        movieRepository.save(movie);
        return movie;
    }

    @Override
    public Movie updateMovie(Long movieId, String title, Integer year) {
        Movie movie = getMovieById(movieId);
        String oldMovieSubDirectoryName = getGeneratedMovieSubFolder(movie.getTitle(), movie.getYear());
        Path moviePath = Path.of(movie.getPath());
        Path newPath = moviePath;
        Movie updatedMovie = getMovieFromApiByTitle(movie, title, year);
        System.out.println(oldMovieSubDirectoryName);
        while (newPath.toString().contains(oldMovieSubDirectoryName)){
            newPath = newPath.getParent();
        }
        String newMovieSubDirectoryName = createGeneratedDirectories(newPath, movie);
        Path renamedFullPath = fileService.renameFile(moviePath, oldMovieSubDirectoryName, newMovieSubDirectoryName+File.separator+getGeneratedMovieFileName(updatedMovie));

        updatedMovie.setPath(renamedFullPath.toString());
        movieRepository.save(updatedMovie);
        return updatedMovie;
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
        TheMovieDb theMovieDb = new TheMovieDb(title, year);
        OmdbApi omdbApi = new OmdbApi(theMovieDb.getImdbId());
        movie.setTitle(omdbApi.getTitle());
        movie.setOriginalTitle(theMovieDb.getOriginalTitle());
        movie.setYear(omdbApi.getYear() == null? year : omdbApi.getYear());
        movie.setRating(omdbApi.getImdbRating() == null? theMovieDb.getRating() : omdbApi.getImdbRating());
        movie.setReleaseDate(omdbApi.getReleasedDate());
        movie.setGenres(getGenres(omdbApi.getGenre()));
        movie.setDirectors(getContributors(omdbApi.getDirector()));
        movie.setWriters(getContributors(omdbApi.getWriter()));
        movie.setActors(getContributors(omdbApi.getActors()));
        movie.setPlot(omdbApi.getPlot() == null || omdbApi.getPlot().equals("N/A")? theMovieDb.getOverview() : omdbApi.getPlot());
        movie.setCountry(omdbApi.getCountry());
        movie.setPoster(omdbApi.getPoster() == null || omdbApi.getPoster().equals("N/A") ? theMovieDb.getPoster() : omdbApi.getPoster());
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

    @Override
    public Page<Movie> organizedDownloadMovieFiles(Pageable page, Path moviesDownloadPath, Path destinationPath){
        Page<Movie> pathMovies = movieRepository.findPathMovies(page, moviesDownloadPath);
        for (Movie movie : pathMovies){
            generateAndMoveMovieFile(movie, destinationPath);
        }
        return pathMovies;
    }

    @Override
    public Page<Movie> getMoviesFromPath(Pageable page, String path) {
        Path movieFilePath = Path.of(path);
        List<Path> videoFiles = fileService.getVideoFiles(movieFilePath);
        for (Path file : videoFiles) {
            Optional <Movie> movieFromDb = movieRepository.findByPath(file.toString());
            if(movieFromDb.isEmpty()) {
                createMovie(file);
            }
        }
        Page<Movie> movies = movieRepository.findPathMovies(page, movieFilePath);
        deleteMovieFromDbWithoutFile(movies.stream().toList());
        return movies;
    }

    //clean movie without file
    private void deleteMovieFromDbWithoutFile(List<Movie> movies){
        for(Movie movie : movies){
            if(!new File(movie.getPath()).exists()){
                movieRepository.delete(movie);
            }
        }
    }

    //create movie with metadata options and details from api
    private void createMovie(Path file){
        List<Audio> audios = audioService.createAudioListFromFile(file);
        List<Subtitle> subtitles = subtitleService.createSubtitleListFromFile(file);
        //get data for movie from Api
        Movie movie = getMovieFromApi(new Movie(), title(file.getFileName().toString()));
        try {
            Instant instant = Files.getLastModifiedTime(file).toInstant();
            LocalDateTime modificationTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            movie.setModificationDate(modificationTime);
        } catch (IOException e) {
            movie.setModificationDate(LocalDateTime.now());
        }


        //get data for movie from ffmpeg metadata
        movie.setPath(file.toString());
        Video video = videoService.createVideoFromPath(file);
        movie.setVideo(video);
        movie.setAudios(audios);
        movie.setSubtitles(subtitles);
        movieRepository.save(movie);
        if(video != null){
            video.setMovie(movie);
            videoService.updateMovieVideo(video.getId(), video, movie);
        }

        for (Subtitle subtitle : subtitles){
            subtitle.setMovie(movie);
            subtitleRepository.save(subtitle);
        }
        for (Audio audio : audios){
            audio.setMovie(movie);
            audioRepository.save(audio);
        }
        if(movie.getTitle() == null){
            movie.setTitle(Path.of(movie.getPath()).getFileName().toString());
            movieRepository.save(movie);
        }
    }

    public Movie generateAndMoveMovieFile(Movie movie, Path destinationPath){
        Path moviePath = Path.of(movie.getPath());
        String moviePathExtension = fileService.getFileExtensionWithDot(moviePath);
        Path destinationFullPath = destinationPath.resolve( createGeneratedDirectories(destinationPath, movie) + File.separator + getGeneratedMovieFileName(movie) + moviePathExtension);
        fileService.moveFile(moviePath, getGeneratedMovieSubFolder(movie.getTitle(), movie.getYear()),destinationFullPath);
        movie.setPath(destinationFullPath.toString());
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> getFiveLastAddedMovies(Pageable pageable) {
        List <Movie> lastFiveAddedMovies = movieRepository.findLastFiveAddedMovies(pageable);
        if(lastFiveAddedMovies.isEmpty()){
            return new ArrayList<>();
        }else{
            return lastFiveAddedMovies;
        }
    }

    @Override
    public List<Movie> getTopFiveMovies(Pageable pageable) {
        List<Movie> topFiveMovies = movieRepository.findTopFiveMovies(pageable);
        if(topFiveMovies.isEmpty()){
            return new ArrayList<>();
        }else {
            return topFiveMovies;
        }
    }

    @Override
    public Page<Movie> filterMovies(Pageable page,
                                    MovieFilterDto movieFilterDto) {
        return movieRepository.filterMovies(
                page,
                movieFilterDto.getTitle(),
                movieFilterDto.getYear(),
                movieFilterDto.getRating(),
                movieFilterDto.getGenre(),
                movieFilterDto.getCountry(),
                movieFilterDto.getDirector(),
                movieFilterDto.getActor(),
                movieFilterDto.getAudio(),
                movieFilterDto.getWriter(),
                movieFilterDto.getSubtitle());
    }

    private String getGeneratedMovieFileName(Movie movie){
        String video = getVideoToString(movie);
        String audio = getAudioToString(movie);
        String country = getCountryToString(movie);
        String genres = getGenresToString(movie);
        String rating = getRatingToString(movie);
        return String.format("%s (%s)%s%s%s%s%s", movie.getTitle().replaceAll("[\\\\/:*?\"<>|]", ""), movie.getYear(), video, audio, country, genres, rating);
    }

    private String getVideoToString(Movie movie){
        return movie.getVideo() == null ? "" :
                String.format(" (%s %s)",movie.getVideo().getResolution(), movie.getVideo().getCodec().getName());
    }

    private String getAudioToString(Movie movie){
        try {
            return movie.getAudios() == null || movie.getAudios().isEmpty() ? "" :
                    String.format(" (%s)", getLanguagesToString(movie.getAudios()));
        }catch (Exception e){
            return "";
        }
    }

    private String getCountryToString(Movie movie){
        return movie.getCountry() == null ? "" :
                String.format(" (Country [%s])", movie.getCountry());
    }

    private String getGenresToString(Movie movie){
        return movie.getGenres() == null || movie.getGenres().isEmpty() ? "" :
                String.format(" (Genres %s)", getGenresToString(movie.getGenres()));
    }

    private String getRatingToString(Movie movie){
        return movie.getRating() == null ? "" :
                String.format(" (Rating %s)", movie.getRating());
    }

    private String getLanguagesToString(List<Audio> languages){
        StringBuilder languagesString = new StringBuilder();
            for (int i = 0; i < languages.size(); i++) {
                languagesString.append(languages.get(i).getLanguage().getIso6392B());
                if (i != languages.size() - 1) {
                    languagesString.append(", ");
                }
            }
            return String.format("[%s]", languagesString);
    }

    private String getGenresToString(List<Genre> genres){
        StringBuilder genresString = new StringBuilder();
        for(int i = 0; i < genres.size(); i++){
            genresString.append(genres.get(i).getTitle());
            if(i != genres.size() - 1){
                genresString.append(", ");
            }
        }return String.format("[%s]",genresString);
    }

    private String getGeneratedMovieSubFolder(String title, Integer year){
        return String.format("%s (%s)", title, year);
    }

    private String createGeneratedDirectories(Path destinationPath, Movie movie){
        String title = movie.getTitle();
        Integer year = movie.getYear();
        String movieDirectory = getGeneratedMovieSubFolder(title, year);
        String[] directories = {movieDirectory};
        fileService.createdDirectories(destinationPath, directories);
        return movieDirectory;
    }

    //try to find movie in api
    private Movie getMovieFromApi(Movie movie, String[] titleElements) {
        for (int i = titleElements.length; i > 0; i--) {
            String[] subArray = Arrays.copyOfRange(titleElements, 0, i);
            try{
                String title = String.join(" ",subArray);
                System.out.println("title: " + title + " year: " + this.year);
                return getMovieFromApiByTitle(movie, title, this.year);
            }catch (JSONException ignored){

            }
        }
        return new Movie();
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
