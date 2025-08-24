package org.sda.mediaporter.Services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.dtos.MovieUpdateDto;
import org.sda.mediaporter.models.Country;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
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
    private MovieService movieService;

    private Resolution hd;
    private Country us;
    private SourcePath downloadsSource;
    private SourcePath externalSource;
    private SourcePath moviesSource;
    private String thorMovieFileName = "Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4";
    private String titanicMovieFileName = "Titanic (1997) ¦ Official Trailer (720p_24fps_H264-128kbit_AAC).mp4";

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
        hd = (Resolution.builder()
                .name("720p")
                .build());

        us = (Country.builder()
                .iso2Code("US")
                .iso3Code("USA")
                .englishName("United States")
                .nativeName("United States")
                .build());

        downloadsSource = (SourcePath.builder()
                        .libraryItem(LibraryItems.MOVIE)
                        .title("Downloads")
                        .pathType(SourcePath.PathType.DOWNLOAD)
                        .path("src\\test\\resources\\movies\\downloads")
                .build());

        moviesSource = (SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .title("External")
                .pathType(SourcePath.PathType.EXTERNAL)
                .path("src\\test\\resources\\movies\\external")
                .build());

        externalSource = (SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .title("Movies")
                .pathType(SourcePath.PathType.SOURCE)
                .path("src\\test\\resources\\movies\\movies")
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void getMoviesFromPath() {
        //Arrest
        sourcePathRepository.save(downloadsSource);
        sourcePathRepository.save(moviesSource);

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
        sourcePathRepository.save(downloadsSource);
        sourcePathRepository.save(moviesSource);
        countryRepository.save(us);

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
        //Arrest
        Movie uncknounMovie = movieRepository.save(Movie.builder()
                .title("notitled (720p_24fps_H264-128kbit_AAC).mp4")
                        .year(2025)
                .path("src/test/resources/movies/notitled (720p_24fps_H264-128kbit_AAC).mp4")
                .build());

        MovieUpdateDto updateDto = MovieUpdateDto.builder()
                .title("Superman")
                .year(2025)
                .build();

        //Act
        movieService.updateMovie(uncknounMovie.getId(), updateDto);

        //Assert
        assertNotNull(uncknounMovie.getReleaseDate());
    }

    @AfterEach
    void redirect_files(){
        //clean sources for next test
        List<Path> testSources = List.of(
                Path.of(downloadsSource.getPath()),
                Path.of(moviesSource.getPath()),
                Path.of(externalSource.getPath()));
        for(Path testSource : testSources) {
            try {
                if (Files.exists(testSource)) {
                    Files.walk(testSource)
                            .sorted(Comparator.reverseOrder()) // delete files first, then directories
                            .forEach(path -> {
                                try {
                                    Files.deleteIfExists(path);
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                            });
                }
                Files.createDirectory(testSource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}