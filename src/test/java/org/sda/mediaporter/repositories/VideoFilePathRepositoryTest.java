package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class VideoFilePathRepositoryTest {

    @Autowired
    private VideoFilePathRepository videoFilePathRepository;

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
    }

    @Test
    void findMoviesVideoFilePathsOlderThan() {
    }

    @Test
    void findMovieIdByVideoFilePathId() {
    }

    @Test
    void deleteVideoFilePathsWithNullFilePath() {
    }

    @Test
    void findMovieVideoFilePathsBySourcePathId() {
    }

    @Test
    void findTvShowEpisodeVideoFilePathsBySourcePathId() {
    }

    @Test
    void deleteAudiosFromVideoFilePath() {
    }

    @Test
    void deleteSubtitlesFromVideoFilePath() {
    }

    @Test
    void findStringFullPathFromVideoFilePathId() {
    }

    @Test
    void findTvShowsVideoFilePathIdsByLibraryItems() {
    }
}