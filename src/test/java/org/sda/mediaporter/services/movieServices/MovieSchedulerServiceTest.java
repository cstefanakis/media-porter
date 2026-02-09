package org.sda.mediaporter.services.movieServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.movieServices.impl.MovieSchedulerServiceImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieSchedulerServiceTest {

    @InjectMocks
    private MovieSchedulerServiceImpl movieSchedulerService;

    @Mock
    private SourcePathService sourcePathService;

    @Mock
    private FileService fileService;

    @Mock
    private MovieService movieService;

    @Mock
    private VideoFilePathService videoFilePathService;

    @Test
    void testMoveMoviesFromDownloadsRootPathToMovieRootPath() {
        // Arrange
        Path fakeVideoPath = Paths.get("Thor.mp4");
        Movie movie = Movie.builder().title("Thor").build();
        VideoFilePath videoFilePath = VideoFilePath.builder().filePath("Thor.mp4").build();

        Configuration configuration = Configuration.builder().maxDatesControlFilesFromExternalSource(10).build();

        SourcePath downloadsSourcePath = SourcePath.builder()
                .path("/downloads")
                .configuration(configuration)
                .build();

        SourcePath moviesSourcePath = SourcePath.builder()
                .path("/movies")
                .configuration(configuration)
                .build();

        when(sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.DOWNLOAD, LibraryItems.MOVIE))
                .thenReturn(List.of(downloadsSourcePath));

        when(sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE))
                .thenReturn(List.of(moviesSourcePath));

        when(fileService.getVideoFiles(any(Path.class))).thenReturn(List.of(fakeVideoPath));
        when(fileService.getModificationLocalDateTimeOfPath(fakeVideoPath))
                .thenReturn(LocalDateTime.now()); // recent file, passes filter

        when(movieService.getOrCreateMovieFromPathFile(any(Path.class))).thenReturn(movie);
        when(videoFilePathService.createVideoFilePath(any(Path.class))).thenReturn(videoFilePath);

        doNothing().when(videoFilePathService).addMovie(any(Movie.class), any(VideoFilePath.class));
        doNothing().when(videoFilePathService).updateSourcePathFileAndPath(any(VideoFilePath.class), any(Movie.class), isNull(), any(Path.class));
        doNothing().when(movieService).updateModificationDateTime(any(Movie.class), any(Path.class));

        //Act
        movieSchedulerService.moveMoviesFromDownloadsRootPathToMovieRootPath();

        verify(movieService).getOrCreateMovieFromPathFile(fakeVideoPath);
        verify(videoFilePathService).createVideoFilePath(fakeVideoPath);
        verify(videoFilePathService).addMovie(movie, videoFilePath);
        verify(videoFilePathService).updateSourcePathFileAndPath(any(VideoFilePath.class), eq(movie), isNull(), any(Path.class));
        verify(movieService).updateModificationDateTime(eq(movie), any(Path.class));
    }

    @Test
    void scanMovieSourcePath() {
        //Act

    }

    @Test
    void moveMoviesFromExternalSources() {
        //Act

    }
}