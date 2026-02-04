package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class VideoFilePathRepositoryTest {

    @Autowired
    private VideoFilePathRepository videoFilePathRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private VideoFilePath tvShowVideoFilePath;

    @BeforeEach
    void setup(){
        this.tvShowVideoFilePath = testDataFactory.createTvShowVideoFilePath();
    }


    @Test
    void findVideoFilePathByPath() {
        //Arrest
        String sourcePathPath = this.tvShowVideoFilePath.getSourcePath().getPath();
        String videoFilePathPath = this.tvShowVideoFilePath.getFilePath();
        String path = Path.of(sourcePathPath + videoFilePathPath).normalize().toString();
        System.out.println(path);
        //Act
        Optional <VideoFilePath> result = videoFilePathRepository.findVideoFilePathByPath(path);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findVideoFilePathByPathAndSourcePath() {
        //Arrest
        SourcePath sourcePath = this.tvShowVideoFilePath.getSourcePath();
        String path = this.tvShowVideoFilePath.getFilePath();
        //Act
        Optional<VideoFilePath> result = videoFilePathRepository.findVideoFilePathByPathAndSourcePath(path, sourcePath);
        //Assert
        assertNotNull(result);
        assertTrue(result.isPresent());

    }

    @Test
    void findMoviesVideoFilePathsOlderThan() {
    }

    @Test
    void findMovieIdByVideoFilePathId() {
    }

    @Test
    void findMovieVideoFilePathsBySourcePathId() {
    }

    @Test
    void findTvShowEpisodeVideoFilePathsBySourcePathId() {
        //Arrest
        Long sourcePathId = this.tvShowVideoFilePath.getSourcePath().getId();
        //Act
        List<VideoFilePath> result = videoFilePathRepository.findTvShowEpisodeVideoFilePathsBySourcePathId(sourcePathId);
        //Assert
        assertNotNull(result);
        assertTrue(result.contains(this.tvShowVideoFilePath));

    }

    @Test
    void deleteAudiosFromVideoFilePath() {
        //Arrest
        List<Long> audios = this.tvShowVideoFilePath.getAudios().stream()
                .map(Audio::getId)
                .toList();
        Long tvShowVideoFilePathId = this.tvShowVideoFilePath.getId();
        //Act
        videoFilePathRepository.deleteAudiosFromVideoFilePath(this.tvShowVideoFilePath);
        VideoFilePath videoFilePath = videoFilePathRepository.findById(tvShowVideoFilePathId).orElse(null);
        assertNotNull(videoFilePath);
        assertNotNull(videoFilePath.getAudios());
        assertTrue(videoFilePath.getAudios().isEmpty());
        audios.forEach(id -> assertFalse(audioRepository.findById(id).isPresent()));
    }

    @Test
    void deleteSubtitlesFromVideoFilePath() {
        //Arrest
        List<Long> subtitles = this.tvShowVideoFilePath.getSubtitles().stream()
                .map(Subtitle::getId)
                .toList();
        Long tvShowVideoFilePathId = this.tvShowVideoFilePath.getId();
        //Act
        videoFilePathRepository.deleteAudiosFromVideoFilePath(this.tvShowVideoFilePath);
        VideoFilePath videoFilePath = videoFilePathRepository.findById(tvShowVideoFilePathId).orElse(null);
        assertNotNull(videoFilePath);
        assertNotNull(videoFilePath.getSubtitles());
        assertTrue(videoFilePath.getAudios().isEmpty());
        subtitles.forEach(id -> assertFalse(audioRepository.findById(id).isPresent()));
    }

    @Test
    void findStringFullPathFromVideoFilePathId() {
        //Arrest
        Long vfpId = this.tvShowVideoFilePath.getId();
        String spPath = this.tvShowVideoFilePath.getSourcePath().getPath();
        String vfpPath = this.tvShowVideoFilePath.getFilePath();
        String path = Path.of(spPath + vfpPath).normalize().toString();
        //Act
        String result = videoFilePathRepository.findStringFullPathFromVideoFilePathId(vfpId);
        //Assert
        assertNotNull(result);
        assertEquals(path, result);
    }

    @Test
    void findTvShowsVideoFilePathIdsByLibraryItems() {
        //Arrest
        Long vfpId = this.tvShowVideoFilePath.getId();
        //Act
        List<Long> result = videoFilePathRepository.findTvShowsVideoFilePathIdsByLibraryItems(LibraryItems.TV_SHOW);
        //Assert
        assertNotNull(result);
        assertTrue(result.contains(vfpId));
    }
}