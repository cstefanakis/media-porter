package org.sda.mediaporter.services.movieServices;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.sda.mediaporter.repositories.VideoFilePathRepository;
import org.sda.mediaporter.services.ContributorService;
import org.sda.mediaporter.services.GenreService;
import org.sda.mediaporter.services.LanguageService;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ContributorService contributorService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private VideoFilePathService videoFilePathService;

    @Autowired
    private VideoFilePathRepository videoFilePathRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @TempDir
    Path tempDir;

    @Test
    void getMovies() {
    }

    @Test
    void getMoviesByTitleAndYear() {
    }

    @Test
    void getMovieById() {
    }

    @Test
    void getMovieByPath() {
    }

    @Test
    void getMovieFromPath_titanic() throws IOException {
        //Arrest
        Path moviePath = this.tempDir.resolve("titanic.1997.mkv");
        Files.createFile(moviePath);
        Long theMovieDbId = 597L;
        String title = "Titanic";

        //Act
        Movie result = movieService.getMovieFromPathFile(moviePath);

        //Assert
        assertEquals(theMovieDbId, result.getTheMovieDbId());
        assertEquals(title, result.getTitle());
    }

    @Test
    void deleteMovieById() {
    }

    @Test
    void getMoviesFromSourcePath() {
    }

    @Test
    void getFiveLastAddedMovies() {
    }

    @Test
    void getTopFiveMovies() {
    }

    @Test
    void filterMovies() {
    }

    @Test
    void filterByAudioLanguage() {
    }

    @Test
    void deleteVideoFilePathFromMovieWithUnveiledPath() {
        //Act
        movieService.deleteVideoFilePathFromMovieWithUnveiledPath();
    }


    @Test
    void deleteMovieVideoFilePath() throws IOException {
        //Arrest
        Path moviePath = this.tempDir.resolve("titanic.1997.mkv");
        Files.createFile(moviePath);
        SourcePath sourcePath = sourcePathRepository.save(SourcePath.builder()
                        .path(this.tempDir.toString())
                        .pathType(SourcePath.PathType.DOWNLOAD)
                        .libraryItem(LibraryItems.MOVIE)
                .build());
        VideoFilePath videoFilePath = videoFilePathRepository.save(VideoFilePath.builder()
                        .filePath(moviePath.getFileName().toString())
                        .sourcePath(sourcePath)
                .build());
        VideoFilePath movieVideoFilePath = videoFilePathService.getVideoFilePathById(videoFilePath.getId());
        //Act
        movieService.deleteMovieVideoFilePath(movieVideoFilePath);
        Optional<VideoFilePath> result = videoFilePathRepository.findById(videoFilePath.getId());
        //Assert
        assertTrue(result.isEmpty());
    }
}