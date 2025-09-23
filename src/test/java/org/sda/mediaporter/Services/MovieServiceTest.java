package org.sda.mediaporter.Services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.dtos.MovieUpdateDto;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.repositories.*;
import org.sda.mediaporter.repositories.metadata.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;


import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class MovieServiceTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private AudioChannelRepository audioChannelRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private MovieService movieService;

    private Resolution hd;
    private Country us;
    private SourcePath downloadsSource;
    private AudioChannel stereo;
    private Codec aac;
    private Codec mp3;
    private Codec h264;
    private Codec h265;
    private Genre action;
    private Language english;
    private Language czech;
    private SourcePath externalSource;
    private SourcePath moviesSource;
    private String thorMovieFileName = "Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4";
    private String titanicMovieFileName = "Titanic (1997) ¦ Official Trailer (720p_24fps_H264-128kbit_AAC).mp4";
    private String notitledFileName = "notitled (720p_24fps_H264-128kbit_AAC).mp4";

    @BeforeEach
    void setup(){
        videoRepository.deleteAll();
        audioRepository.deleteAll();
        movieRepository.deleteAll();
        configurationRepository.deleteAll();
        sourcePathRepository.deleteAll();
        resolutionRepository.deleteAll();
        languageRepository.deleteAll();
        codecRepository.deleteAll();
        audioChannelRepository.deleteAll();
        genreRepository.deleteAll();
        countryRepository.deleteAll();

        //create resolutions
        this.mp3 = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("MP3")
                .build());

        this.aac = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("AAC")
                .build());

        this.h264 = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.VIDEO)
                .name("H264")
                .build());

        this.h265 = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.VIDEO)
                .name("H265")
                .build());

        this.action = genreRepository.save(Genre.builder()
                .title("Action")
                .build());

        this.hd = resolutionRepository.save(Resolution.builder()
                .name("720p")
                .build());

        this.us = countryRepository.save(Country.builder()
                .iso2Code("US")
                .iso3Code("USA")
                .englishName("United States")
                .nativeName("United States")
                .build());

        this.czech = languageRepository.save(Language.builder()
                .iso6391("cs")
                .iso6392B("cze")
                .iso6392T("ces")
                .englishTitle("Czech")
                .originalTitle("Čeština")
                .build());

        this.english = languageRepository.save(Language.builder()
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .englishTitle("English")
                .originalTitle("English")
                .build());

        this.downloadsSource = sourcePathRepository.save((SourcePath.builder()
                        .libraryItem(LibraryItems.MOVIE)
                        .title("Downloads")
                        .pathType(SourcePath.PathType.DOWNLOAD)
                        .path("src/test/resources/movies/downloads")
                .build()));

        this.externalSource = sourcePathRepository.save((SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .title("External")
                .pathType(SourcePath.PathType.EXTERNAL)
                .path("src\\test\\resources\\movies\\external")
                .build()));

        this.moviesSource = sourcePathRepository.save((SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .title("Movies")
                .pathType(SourcePath.PathType.SOURCE)
                .path("src\\test\\resources\\movies\\movies")
                .build()));

        this.stereo = audioChannelRepository.save(AudioChannel.builder()
                .title("2 Stereo")
                .channels(2)
                .description("Two-channel stereo sound")
                .build());

        languageRepository.save(Language.builder()
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .englishTitle("English")
                .originalTitle("English")
                .build());

        languageRepository.save(Language.builder()
                .iso6391("sv")
                .iso6392B("swe")
                .iso6392T("swe")
                .englishTitle("Swedish")
                .originalTitle("Svenska")
                .build());

        languageRepository.save(Language.builder()
                .iso6391("it")
                .iso6392B("ita")
                .iso6392T("ita")
                .englishTitle("Italian")
                .originalTitle("Italiano")
                .build());


        String rootSourceOfMovies = "src\\test\\resources\\movies";

        try{
            Files.copy(Path.of(rootSourceOfMovies + File.separator + thorMovieFileName),
                    Path.of( downloadsSource.getPath()
                            + File.separator
                            + thorMovieFileName));
            Files.copy(Path.of(rootSourceOfMovies + File.separator + titanicMovieFileName),
                    Path.of(downloadsSource.getPath()
                            + File.separator
                            + titanicMovieFileName));
            Files.copy(Path.of("src/test/resources/movies/notitled (720p_24fps_H264-128kbit_AAC).mp4"),
                    Path.of("src/test/resources/movies/downloads/notitled (720p_24fps_H264-128kbit_AAC).mp4"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void getMoviesFromPath() {
        //Arrest
        Pageable page = Pageable.ofSize(10);
        List<SourcePath> downloadPaths = sourcePathRepository.findSourcePathsByPathType(SourcePath.PathType.DOWNLOAD);

        //Act
        List<Movie> pathMovies = movieService.getMoviesFromPath(page, downloadPaths.getFirst().getPath())
                .getContent();

        Movie movie = movieRepository.findByPath(downloadsSource.getPath()
                + File.separator
                + thorMovieFileName)
                .orElse(null);


        //Assert
        assertEquals(2, pathMovies.size());
        assertNotNull(movie);
        assertEquals("Thor", movie.getTitle());
        assertEquals(2011, movie.getYear());
        assertEquals("United States", movie.getCountries().getFirst().getEnglishName());
        assertTrue(movie.getRating() > 0);
        //Video
        assertNotNull(movie.getVideo().getResolution().getName());
        assertNotNull(movie.getVideo().getCodec().getName());
        assertTrue(movie.getVideo().getBitrate() > 0);

        //Audio
        assertFalse(movie.getAudios().isEmpty());
        assertNull(movie.getAudios().getFirst().getLanguage());
        assertNotNull(movie.getAudios().getFirst().getBitrate());
        assertNotNull(movie.getAudios().getFirst().getCodec().getName());

        assertFalse(movie.getGenres().isEmpty());
        assertNotNull(movie.getLanguages().getFirst().getEnglishTitle());
        assertFalse(movie.getDirectors().isEmpty());
        assertFalse(movie.getWriters().isEmpty());
        assertFalse(movie.getActors().isEmpty());
        assertNotNull(movie.getReleaseDate());
        assertNotNull(movie.getPlot());
        assertNotNull(movie.getPoster());
    }

    @Test
    void getMoviesFromPath_withAllEntities() {
        //Arrest

        Pageable page = Pageable.ofSize(10);
        List<SourcePath> downloadPaths = sourcePathRepository.findSourcePathsByPathType(SourcePath.PathType.DOWNLOAD);

        //Act
        List<Movie> pathMovies = movieService.getMoviesFromPath(page, downloadPaths.getFirst().getPath())
                .getContent();

        //Assert
        assertEquals(2, pathMovies.size());
    }

    @Test
    void getMovies() {
        //Arrest
        movieRepository.save(Movie.builder()
                        .originalTitle("Name of the Game")
                        .title("Name of the Game")
                        .year(2025)
                .build());

        movieRepository.save(Movie.builder()
                .originalTitle("Americana")
                .title("Americana")
                .year(2025)
                        .path("src/test/resources/movies/downloads/Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4")
                .build());


        Pageable page = Pageable.ofSize(10);
        //Act
        List<Movie> movies = movieService.getMovies(page).getContent();

        //Assert
        assertEquals(1, movies.size());
    }

    @Test
    void updateMovie(){
        //Assert
        assertNotNull(this.downloadsSource.getId());
        assertEquals("src/test/resources/movies/downloads", this.downloadsSource.getPath());

        Movie uncknounMovie = movieRepository.save(Movie.builder()
                .title("notitled (720p_24fps_H264-128kbit_AAC).mp4")
                        .year(2025)
                .path("src/test/resources/movies/downloads/notitled (720p_24fps_H264-128kbit_AAC).mp4")
                .build());
        //Assert
        assertNotNull(uncknounMovie.getId());
        assertEquals("src/test/resources/movies/downloads/notitled (720p_24fps_H264-128kbit_AAC).mp4", uncknounMovie.getPath());
        assertTrue(uncknounMovie.getPath().contains(this.downloadsSource.getPath()));

        MovieUpdateDto updateDto = MovieUpdateDto.builder()
                .title("Superman")
                .year(2025)
                .build();

        //Act
        movieService.updateMovie(uncknounMovie.getId(), updateDto);
        Movie updatedMovie = movieRepository.findById(uncknounMovie.getId()).orElse(null);

        //Assert
        assertNotNull(updatedMovie);
        assertEquals(Path.of("src\\test\\resources\\movies\\downloads\\Superman (2025)\\Superman (2025) (Country [United States, Canada, Australia, New Zealand]) (Genres [Action, Adventure, Sci-fi]) (Rating 7.558).mp4").normalize(), Path.of(updatedMovie.getPath()).normalize());
        assertEquals("Superman", updatedMovie.getTitle());
        assertEquals(2025, updatedMovie.getYear());
        assertNotNull(updatedMovie.getPlot());
        assertNotNull(updatedMovie.getWriters());
        assertNotNull(updatedMovie.getDirectors());
        assertNotNull(updatedMovie.getActors());
        assertNotNull(updatedMovie.getLanguages());
        assertNotNull(updatedMovie.getReleaseDate());
        assertNotNull(updatedMovie.getCountries());
        assertNotNull(updatedMovie.getGenres());
        assertNotNull(updatedMovie.getOriginalTitle());
        assertNotNull(updatedMovie.getModificationDate());
        assertNotNull(updatedMovie.getRating());
    }

    @Test
    void moveMovie(){
        //Arrest
        Movie thor = movieRepository.save(Movie.builder()
                        .title("Thor")
                        .year(2011)
                        .path(downloadsSource.getPath()
                                + File.separator
                                + thorMovieFileName)
                .build());
        Path destinationPath = Path.of("src\\test\\resources\\movies\\movies\\Thor (2011)\\Thor (2011).mp4");
        //Act
        thor = movieService.moveMovie(thor.getId(), Path.of(this.moviesSource.getPath()));
        //Assert
        assertEquals(destinationPath.toString(), thor.getPath());
        assertTrue(Files.exists(destinationPath));

    }

    @Test
    void moveMovie_withNoYear(){
        //Arrest
        Movie thor = movieRepository.save(Movie.builder()
                .title("Thor")
                .path(downloadsSource.getPath()
                        + File.separator
                        + thorMovieFileName)
                .build());
        Path destinationPath = Path.of("src\\test\\resources\\movies\\movies\\Thor\\Thor.mp4");
        //Act
        thor = movieService.moveMovie(thor.getId(), Path.of(this.moviesSource.getPath()));
        //Assert
        assertEquals(destinationPath.toString(), thor.getPath());
        assertTrue(Files.exists(destinationPath));
    }

    @Test
    void moveMoviesFromDownloadPathsToMoviesPath(){
        //Act
        movieService.moveMoviesFromDownloadPathsToMoviesPath();
    }

//    @Test
//    void autoCopyMoviesFromExternalSource() {
//        //Arrest
//        List<Path> testSources = List.of(
//                Path.of(downloadsSource.getPath()),
//                Path.of(moviesSource.getPath()),
//                Path.of(externalSource.getPath()));
//        for(Path testSource : testSources) {
//            try {
//                if (Files.exists(testSource)) {
//                    Files.walk(testSource)
//                            .sorted(Comparator.reverseOrder())
//                            .forEach(path -> {
//                                try {
//                                    Files.deleteIfExists(path);
//                                } catch (IOException e) {
//                                    throw new UncheckedIOException(e);
//                                }
//                            });
//                }
//                Files.createDirectory(testSource);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        List<Path> files = fileService.getVideoFiles(Path.of("src/test/resources/movies"));
//        files.forEach(p -> {
//            fileService.copyFile(p, Path.of(
//                    this.externalSource.getPath()
//                            + File.separator
//                            + p.getFileName()));
//                });
//
//        Configuration configuration = configurationRepository.save(Configuration.builder()
//                .id(1L)
//                .maxDatesSaveFile(9000)
//                .maxDatesControlFilesFromExternalSource(0)
//                .videoResolutions(List.of(this.hd))
//                .firstVideoBitrateValueRange(0)
//                .secondVideoBitrateValueRange(200000000)
//                .firstAudioBitrateValueRange(0)
//                .secondAudioBitrateValueRange(2048000)
//                .audioChannels(List.of(this.stereo))
//                .firstVideoSizeRange(0.0)
//                .secondVideoSizeRange(31457280.0)
//                .audioCodecs(List.of(this.aac))
//                .videoCodecs(List.of(this.h264))
//                .genres(List.of(this.action))
//                .audioLanguages(List.of(this.english))
//                .build());
//
//        //Act
//        movieService.autoCopyMoviesFromExternalSource();
//    }

//    @AfterEach
//    void redirect_files(){
//        //clean sources for next test
//        List<Path> testSources = List.of(
//                Path.of(downloadsSource.getPath()),
//                Path.of(moviesSource.getPath()),
//                Path.of(externalSource.getPath()));
//        for(Path testSource : testSources) {
//            try {
//                if (Files.exists(testSource)) {
//                    Files.walk(testSource)
//                            .sorted(Comparator.reverseOrder())
//                            .forEach(path -> {
//                                try {
//                                    Files.deleteIfExists(path);
//                                } catch (IOException e) {
//                                    throw new UncheckedIOException(e);
//                                }
//                            });
//                }
//                Files.createDirectory(testSource);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}