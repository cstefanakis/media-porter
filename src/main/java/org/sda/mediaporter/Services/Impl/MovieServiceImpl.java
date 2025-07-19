package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.json.JSONException;
import org.sda.mediaporter.Services.*;
import org.sda.mediaporter.api.OmdbApi;
import org.sda.mediaporter.api.TheMovieDb;
import org.sda.mediaporter.dtos.MovieFilterDto;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.enums.*;
import org.sda.mediaporter.models.metadata.*;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MovieServiceImpl implements MovieService {

    private final GenreService genreService;
    private final ContributorService contributorService;
    private final LanguageService languageService;
    private final LanguageRepository languageRepository;
    private final MovieRepository movieRepository;
    private final FileService fileService;
    private final AudioService audioService;
    private final VideoService videoService;
    private final SubtitleService subtitleService;
    private final SourcePathRepository sourcePathRepository;
    private final ConfigurationService configurationService;
    private final CodecRepository codecRepository;
    private final CountryService countryService;

    private Integer year;

    @Autowired
    public MovieServiceImpl(GenreService genreService, ContributorService contributorService, LanguageService languageService, LanguageRepository languageRepository, MovieRepository movieRepository, FileService fileService, AudioService audioService, VideoService videoService, SubtitleService subtitleService, SourcePathRepository sourcePathRepository, ConfigurationService configurationService, CodecRepository codecRepository, CountryService countryService) {
        this.genreService = genreService;
        this.contributorService = contributorService;
        this.languageService = languageService;
        this.languageRepository = languageRepository;
        this.movieRepository = movieRepository;
        this.fileService = fileService;
        this.audioService = audioService;
        this.videoService = videoService;
        this.subtitleService = subtitleService;
        this.sourcePathRepository = sourcePathRepository;
        this.configurationService = configurationService;
        this.codecRepository = codecRepository;
        this.countryService = countryService;
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
    public List<Movie> getMovieByTitleAndYear(String title, Integer year) {
        return movieRepository.findMovieByTitleAndYear(title, year);
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
        movie.setCountries(getCountries(omdbApi));
        movie.setPoster(omdbApi.getPoster() == null || omdbApi.getPoster().equals("N/A") ? theMovieDb.getPoster() : omdbApi.getPoster());
        movie.setLanguages(getLanguagesByTitle(omdbApi.getLanguages()));
        return movie;
    }

    private List<Country> getCountries(OmdbApi omdbApi){
        String countriesFromMovie = omdbApi.getCountry();
        if(countriesFromMovie == null){
            return null;
        }
        System.out.println(countriesFromMovie);
        String[] countries = countriesFromMovie.split(",");
        try{
            return Arrays.stream(countries).map(countryTitle -> countryService.getCountryByName(countryTitle)).toList();
        }catch (EntityNotFoundException e) {
            return Arrays.stream(countries).map(countryCode -> countryService.getCountryByCode(countryCode)).toList();
        }
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
            if(contributor != null){
                contributors.add(contributorService.autoCreateContributor(contributor));
            }
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
        //get data for movie from Api
        Movie movie = getMovieFromApi(new Movie(), title(file.getFileName().toString()));
        movie.setModificationDate(getModificationTimeFromFile(file));
        movie.setPath(file.toString());
        if(movie.getTitle() == null){
            movie.setTitle(file.getFileName().toString());
        }
        movie = movieRepository.save(movie);
        //get data for movie from ffmpeg metadata
        videoService.createVideoFromPath(file, movie);
        audioService.createAudioListFromFile(file, movie);
        subtitleService.createSubtitleListFromFile(file, movie);
    }

    private LocalDateTime getModificationTimeFromFile(Path file){
        try {
            Instant instant = Files.getLastModifiedTime(file).toInstant();
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (IOException e) {
            return LocalDateTime.now();
        }
    }

    public Movie generateAndMoveMovieFile(Movie movie, Path destinationPath){
        Path moviePath = Path.of(movie.getPath());
        String movieTitle = movie.getTitle() == null? "Untitled_Movies" : movie.getTitle();
        String moviePathExtension = fileService.getFileExtensionWithDot(moviePath);
        Path untitledDestinationPath = moviePath.resolve(movieTitle + File.separator + moviePath.getFileName());
        Path destinationFullPath = movie.getTitle() == null? untitledDestinationPath : destinationPath.resolve(createGeneratedDirectories(destinationPath, movie) + File.separator + getGeneratedMovieFileName(movie) + moviePathExtension);
        fileService.moveFile(moviePath, getGeneratedMovieSubFolder(movieTitle, movie.getYear()), destinationFullPath);
        movie.setPath(destinationFullPath.toString());
        return movieRepository.save(movie);

    }

    @Override
    public Page<Movie> getFiveLastAddedMovies(Pageable pageable) {
        return movieRepository.findLastFiveAddedMovies(pageable);
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
            genres = genreService.getAllGenres().stream().map(g-> g.getId()).toList();
        }

        List<Long> countries = movieFilterDto.getCountryIds();
        if(countries == null){
            countries = countryService.getAllCountries().stream().map(c-> c.getId()).toList();
        }

        List<Long> audioLanguages = movieFilterDto.getALanguageIds();
//        if(audioLanguages == null){
//            audioLanguages = languageService.getAllLanguages().stream().map(a -> a.getId()).toList();
//        }
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
    @Async
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void moveMoviesFromDownloadPathsToMoviesPath() {
            try {
                List<SourcePath> movieDownloadPaths = sourcePathRepository.findSourcePathsByPathType(SourcePath.PathType.DOWNLOAD);
                SourcePath moviesPath = sourcePathRepository.findSourcePathsByPathType(SourcePath.PathType.SOURCE).getFirst();
                for (SourcePath path : movieDownloadPaths) {
                    List<Path> videoFiles = fileService.getVideoFiles(Path.of(path.getPath()));
                    for (Path file : videoFiles) {
                        createGenerateAndMoveFile(file, moviesPath);
                    }
                }
            }catch (NoSuchElementException e){
                throw new RuntimeException("no sources sets");
            }
    }

    private void createGenerateAndMoveFile(Path file, SourcePath moviesPath){
        Optional<Movie> movieFromDb = movieRepository.findByPath(file.toString());
        if (movieFromDb.isEmpty()) {
            createMovie(file);
        }
        Movie movie = movieRepository.findByPath(file.toString()).orElseThrow(() -> new RuntimeException("Movie not found for path: " + file));
        generateAndMoveMovieFile(movie, Path.of(moviesPath.getPath()));
    }

    @Override
    @Async
    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000)
    public void autoDeleteMoviesByProperties() {
        int maxDaysToSave = configurationService.getConfiguration().getMaxDatesSaveFile();
        List<Movie> movies = movieRepository.findMoviesOlderThan(LocalDateTime.now().minusDays(maxDaysToSave));
        movies.forEach(m-> deleteMovieById(m.getId()));
    }

    @Override
    @Async
    @Scheduled(fixedDelay =  30 * 60 * 1000)
    public void autoCopyMoviesFromExternalSource() {
        Configuration configuration = configurationService.getConfiguration();
        List<SourcePath> externalSourcePaths = sourcePathRepository.findSourcePathsByPathType(SourcePath.PathType.EXTERNAL);
        Path downloadPath = Path.of(sourcePathRepository.findSourcePathsByPathType(SourcePath.PathType.DOWNLOAD).getFirst().getPath());
        SourcePath sourceMoviePath = sourcePathRepository.findSourcePathsByPathType(SourcePath.PathType.SOURCE).getFirst();
        int maxDatesControlFilesFromExternalSource = configuration.getMaxDatesControlFilesFromExternalSource();

        for(SourcePath externalPath : externalSourcePaths) {

            System.out.println(externalPath.getPath());

            Path path = Path.of(externalPath.getPath());
            List <Path> videoFiles = fileService.getVideoFiles(path);
            System.out.println(videoFiles);
            for(Path videoFile : videoFiles){

                System.out.println(videoFile);

                LocalDateTime pathLocalDateTime = fileService.getModificationLocalDateTimeOfPath(videoFile);
                LocalDateTime videoRangeLocalDateTime = LocalDateTime.now().minusDays(maxDatesControlFilesFromExternalSource);

                if(pathLocalDateTime.isAfter(videoRangeLocalDateTime)){

                    System.out.println("Date ok");

                    Movie movie = getMovieFromApi(new Movie(), title(videoFile.getFileName().toString()));
                    List<Movie> movies = getMovieByTitleAndYear(movie.getTitle(), movie.getYear());

                    if(movies.isEmpty()) {

                        System.out.println("not exist");

                        if (isIncludedGenres(configuration, movie)) {

                            System.out.println("include genre");

                            List<Audio> audios = audioService.getAudioListFromFile(videoFile);
                            Video video = videoService.getVideoFromPath(videoFile);
                            if (isIncludedAudios(configuration, audios) && isIncludedVideo(configuration, video)) {

                                fileService.copyFile(videoFile, Path.of(downloadPath + File.separator + videoFile.getFileName()));
                                createGenerateAndMoveFile(Path.of(downloadPath + File.separator + videoFile.getFileName()), sourceMoviePath);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isIncludedGenres(Configuration configuration, Movie movie){
        List<Genre> movieGenres = movie.getGenres();
        List<Genre> configurationGenres = configurationService.getGenresFromConfiguration(configuration);
        return configurationGenres.retainAll(movieGenres);
    }


    private boolean isIncludedVideo(Configuration configuration, Video video){

        //Codec check
        Codec movieCodec = video.getCodec();
        List<Codec> configurationVideoCodecs = configurationService.getVideoCodecsFromConfiguration(configuration);

        for(Codec configVideoCodec : configurationVideoCodecs) {

            System.out.println("check video codec");
            //check if the configuration includes audio codecs from movie properties
            if(movieCodec != null && configVideoCodec.getName().equals(movieCodec.getName())){

                System.out.println("video codecs: " + configVideoCodec.getName() + " " + movieCodec.getName());

                //Resolution check
                Resolution movieResolution = video.getResolution();
                List<Resolution> configurationResolutions = configurationService.getVideoResolutionFromConfiguration(configuration);
                for(Resolution configResolution : configurationResolutions){

                    System.out.println("check video resolution");

                    //check if the configuration includes resolutions from movie properties
                    if(movieResolution != null && configResolution.getName().equals(movieResolution.getName())){

                        System.out.println("video resolutions: " + configResolution.getName() + " - " + movieResolution.getName());

                        //Video bitrate check
                        System.out.println("check video bitrate");
                        Integer firstVideoBitrateRange = configuration.getFirstVideoBitrateValueRange();
                        Integer secondVideoBitrateRange = configuration.getSecondVideoBitrateValueRange();
                        Integer movieVideoBitrate = video.getBitrate();

                        System.out.println(firstVideoBitrateRange + " (" + movieVideoBitrate + ") " + secondVideoBitrateRange);

                        if(movieVideoBitrate == null || (movieVideoBitrate > firstVideoBitrateRange && movieVideoBitrate < secondVideoBitrateRange)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isIncludedAudios(Configuration configuration, List<Audio> audios){

        for(Audio movieAudio : audios){
            //check audio codec
            Codec movieAudioCodec = movieAudio.getCodec();
            List<Codec> configAudioCodecs = configurationService.getAudioCodecsFromConfiguration(configuration);
            System.out.println("include audio codec");

            for(Codec configCode : configAudioCodecs){
                if(movieAudioCodec != null && movieAudioCodec.getName().equals(configCode.getName())) {
                    //check audio language
                    List<Language> configAudioLanguages = configurationService.getLanguagesFromConfiguration(configuration);
                    Language movieAudioLanguage = movieAudio.getLanguage();
                    for (Language configLanguage : configAudioLanguages) {
                        System.out.println("check language" + movieAudioLanguage.getEnglishTitle() + " - " + configLanguage.getEnglishTitle());

                        //check if the language is in configuration and the movie
                        if (movieAudioLanguage != null && movieAudioLanguage.getEnglishTitle().equals(configLanguage.getEnglishTitle())) {
                            System.out.println("language title: " + movieAudioLanguage.getEnglishTitle() + " " + configLanguage.getEnglishTitle());
                            //check audio channel
                            List<AudioChannel> configAudioChannels = configurationService.getAudioChannelsFromConfiguration(configuration);
                            AudioChannel movieAudioChannel = movieAudio.getAudioChannel();
                            for (AudioChannel configChannel : configAudioChannels) {
                                System.out.println("check channel:");
                                //check if audio channels is in configuration and the movie
                                if (movieAudioChannel != null && movieAudioChannel.getChannels().equals(configChannel.getChannels())) {
                                    System.out.println("channels: " + movieAudioChannel.getChannels() + " - " + configChannel.getChannels());
                                    //check audio bitrate
                                    Integer firstAudioBitrate = configuration.getFirstAudioBitrateValueRange();
                                    Integer secondAudioBitrate = configuration.getSecondAudioBitrateValueRange();
                                    Integer movieAudioBitrate = movieAudio.getBitrate();
                                    System.out.println("audio bitrate: " + firstAudioBitrate + " (" + movieAudioBitrate + ") " + secondAudioBitrate);
                                    if(movieAudioBitrate == null || (movieAudioBitrate > firstAudioBitrate && movieAudioBitrate < secondAudioBitrate)){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }



    @Override
    @Async
    @Scheduled(fixedDelay = 14 * 60 * 60 * 1000)
    public void autoLoadMoviesFromLocalSources(){
        Pageable pageable = PageRequest.of(0, 10);
        List<SourcePath> localMovieSources = sourcePathRepository.findSourcePathsByPathType(SourcePath.PathType.SOURCE);
        for(SourcePath sourcePath : localMovieSources) {
            getMoviesFromPath(pageable, sourcePath.getPath());
        }
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
        String videoResolution = movie.getVideo().getResolution() == null ? "" : movie.getVideo().getResolution().getName();
        String videoCodec = movie.getVideo().getCodec() == null ? "" : movie.getVideo().getCodec().getName();
        return videoResolution.isEmpty() && videoCodec.isEmpty() ? "" :
                String.format(" (%s %s)",videoResolution, videoCodec);
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
        List<Country> countries = movie.getCountries();
        String text = countries.stream()
                .map(Country::getEnglishName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        return String.format(" (Country [%s])", text);
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
        String titleWithoutIllegalChar = title.replaceAll("[\\\\/:*?\"<>|]", "");
        return String.format("%s (%s)", titleWithoutIllegalChar, year);
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
        return languageRepository.findByCode(element).isPresent();
    }

    //check if element is codec
    private boolean isCodec(String element){
        return codecRepository.findByName(element).isPresent();
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
