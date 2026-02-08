package org.sda.mediaporter.services.fileServices;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.metadata.*;
import org.sda.mediaporter.repositories.*;
import org.sda.mediaporter.services.audioServices.AudioService;
import org.sda.mediaporter.services.fileServices.impl.VideoFilePathServiceImpl;
import org.sda.mediaporter.services.subtitleServices.SubtitleService;
import org.sda.mediaporter.services.videoServices.VideoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoFilePathServiceTest {

    @Mock
    private VideoFilePathRepository videoFilePathRepository;

    @Mock
    private VideoService videoService;

    @Mock
    private AudioService audioService;

    @Mock
    private SubtitleService subtitleService;

    @Mock
    private SourcePathService sourcePathService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private VideoFilePathServiceImpl videoFilePathService;

    @TempDir
    Path tvShowSource;

    @TempDir
    Path tvShowDownloads;


    @BeforeEach
    void setup() throws IOException {
        Path tvShowDownloadedPath =  this.tvShowDownloads.resolve("wonder man s01e01 (Cz dabbing)").normalize();
        Files.createFile(tvShowDownloadedPath);
    }

    @Test
    void getVideoFilePathByPath() {
        //Arrest
        VideoFilePath videoFilePath = VideoFilePath.builder().build();
        String pathToString = this.tvShowSource.resolve("filePath").toString();
        when(videoFilePathRepository.findVideoFilePathByPath(pathToString)).thenReturn(Optional.of(videoFilePath));
        //Act
        VideoFilePath result = videoFilePathService.getVideoFilePathByPath(pathToString);
        //Assert
        assertNotNull(result);
        verify(videoFilePathRepository).findVideoFilePathByPath(pathToString);
    }

    @Test
    void getVideoFilePathById_Found() {
        // Arrange
        Long id = 1L;
        VideoFilePath videoFilePath = VideoFilePath.builder().build();
        when(videoFilePathRepository.findById(id))
                .thenReturn(Optional.of(videoFilePath));
        // Act
        VideoFilePath result = videoFilePathService.getVideoFilePathById(id);
        // Assert
        assertNotNull(result);
        assertEquals(videoFilePath, result);
        verify(videoFilePathRepository).findById(id);
    }

    @Test
    void getVideoFilePathById_NotFound() {
        // Arrange
        Long id = 1L;
        when(videoFilePathRepository.findById(id))
                .thenReturn(Optional.empty());
        // Assert
        assertThrows(EntityNotFoundException.class,
                () -> videoFilePathService.getVideoFilePathById(id));
        verify(videoFilePathRepository).findById(id);
    }

    @Test
    void getVideoFilePath() throws IOException {
        // Arrange
        LocalDateTime localDateTime = LocalDateTime.now();

        Path filePath = this.tvShowSource.resolve("filePath.mp4");
        Files.createFile(filePath);

        SourcePath sourcePath = SourcePath.builder()
                .path(this.tvShowSource.toString())
                .pathType(SourcePath.PathType.SOURCE)
                .libraryItem(LibraryItems.TV_SHOW)
                .build();

        Video video = Video.builder()
                .codec(Codec.builder().build())
                .resolution(Resolution.builder().build())
                .build();

        List<Audio> audios = new ArrayList<>();
        List<Subtitle> subtitles = new ArrayList<>();

        when(sourcePathService.getSourcePathFromPath(filePath))
                .thenReturn(sourcePath);

        when(videoService.getVideoFromPath(eq(filePath), any(VideoFilePath.class)))
                .thenReturn(video);

        when(audioService.getAudiosFromPathFile(eq(filePath), any(VideoFilePath.class)))
                .thenReturn(audios);

        when(subtitleService.getSubtitleListFromFile(eq(filePath), any(VideoFilePath.class)))
                .thenReturn(subtitles);

        when(fileService.getModificationLocalDateTimeOfPath(filePath))
                .thenReturn(localDateTime);

        // Act
        VideoFilePath result = videoFilePathService.getVideoFilePath(filePath);

        // Assert
        assertNotNull(result);
        assertEquals("\\\\filePath.mp4", result.getFilePath());
        assertEquals(localDateTime, result.getModificationDateTime());
        assertEquals(video, result.getVideo());
        assertEquals(audios, result.getAudios());
        assertEquals(subtitles, result.getSubtitles());
    }


    @Test
    void createVideoFilePath() {
    }

    @Test
    void updateSourcePathFileAndPath() {
    }

    @Test
    void deleteVideoFilePath() {
    }

    @Test
    void getAllVideoFilePaths() {
    }

    @Test
    void isVideoFilePathWithPathExist() {
    }

    @Test
    void addMovie() {
    }

    @Test
    void getFullPathFromVideoFilePath() {
    }

    @Test
    void addTvShowEpisode() {
    }

    @Test
    void getVideoFileNamePart() {
    }

    @Test
    void getAudioFileNamePart() {
    }

    @Test
    void getFilePathWithoutSourcePath() {
    }

    @Test
    void getMoviesVideoFilesPathsOlderThanXDays() {
    }

    @Test
    void getMovieIdByVideoFilePathId() {
    }

    @Test
    void testDeleteVideoFilePathAndFileByVideoFilePathId() {
    }

    @Test
    void deleteVideoFilePathsWithNullFilePath() {
    }

    @Test
    void getMovieVideoFilePathsBySourcePathId() {
    }

    @Test
    void setVideoFilePathMovieNull() {
    }

    @Test
    void testDeleteAllDataFromVideoFilePath() {
    }

    @Test
    void testGetFullPathFromVideoFilePathId() {
    }

    @Test
    void testGetTvShowsVideoFilePathIdsByLibraryItems() {
    }

    @Test
    void deleteVideoFilePathById() {
    }
}