package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.TvShowEpisode;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class TvShowEpisodeRepositoryTest {

    @Autowired
    private TvShowEpisodeRepository tvShowEpisodeRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Long tvShowId;
    private VideoFilePath videoFilePath;
    private TvShowEpisode tvShowEpisodeWithoutVideoFilePath;

    @BeforeEach
    void loadData(){
        this.videoFilePath = testDataFactory.createTvShowVideoFilePath();
        this.tvShowId = this.videoFilePath.getTvShowEpisode().getTvShow().getId();
        this.tvShowEpisodeWithoutVideoFilePath = testDataFactory.createTvShowEpisodeWithoutVideoFilePath();
    }

    @Test
    void findTvShowEpisodeByTheMovieDbId() {
        //Arrest
        Long theMovieDbId = 1L;
        //Act
        Optional<TvShowEpisode> result = tvShowEpisodeRepository.findTvShowEpisodeByTheMovieDbId(theMovieDbId);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findTvShowEpisodeByPath() {
        //Arrest
        String sourcePathPath = this.videoFilePath.getSourcePath().getPath();
        String videoFilePathPath = this.videoFilePath.getFilePath();
        String tvShowEpisodePath = Path.of(sourcePathPath + videoFilePathPath).normalize().toString();
        System.out.println(tvShowEpisodePath);
        //Act
        Optional<TvShowEpisode> result = tvShowEpisodeRepository.findTvShowEpisodeByPath(tvShowEpisodePath);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findTvShowEpisodeIdByVideoFilePathId() {
        //Arrest
        Long videoFilePathId = this.videoFilePath.getId();
        Long tvShowId = this.tvShowId;
        //Act
        Long result = tvShowEpisodeRepository.findTvShowEpisodeIdByVideoFilePathId(videoFilePathId);
        //Assert
        assertNotNull(result);
        assertEquals(tvShowId, result);
    }

    @Test
    void deleteTvShowEpisodeWithoutVideoFilePaths() {
        //Arrest
        Long tvShowEpisodeId = this.tvShowEpisodeWithoutVideoFilePath.getId();
        Optional <TvShowEpisode> tvShowEpisode = tvShowEpisodeRepository.findById(tvShowEpisodeId);
        assertTrue(tvShowEpisode.isPresent());
        assertTrue(tvShowEpisode.get().getVideoFilePaths().isEmpty());
        //Act
        tvShowEpisodeRepository.deleteTvShowEpisodeWithoutVideoFilePaths(tvShowEpisodeId);
        //Assert
        assertFalse(tvShowEpisodeRepository.findById(tvShowEpisodeId).isPresent());
    }

    @Test
    void deleteTvShowEpisodesWithoutVideoFilePaths() {
        //Arrest
        Long tvShowEpisodeId = this.tvShowEpisodeWithoutVideoFilePath.getId();
        Optional <TvShowEpisode> tvShowEpisode = tvShowEpisodeRepository.findById(tvShowEpisodeId);
        assertTrue(tvShowEpisode.isPresent());
        assertTrue(tvShowEpisode.get().getVideoFilePaths().isEmpty());
        //Act
        tvShowEpisodeRepository.deleteTvShowEpisodesWithoutVideoFilePaths();
        //Assert
        assertFalse(tvShowEpisodeRepository.findById(tvShowEpisodeId).isPresent());
    }

    @Test
    void isExistTvShowEpisodeWithTheMovieDbId_true() {
        //Arrest
        Long theMovieDbId = this.tvShowEpisodeWithoutVideoFilePath.getTheMovieDbId();
        //Act
        boolean result = tvShowEpisodeRepository.isExistTvShowEpisodeWithTheMovieDbId(theMovieDbId);
        //Assert
        assertTrue(result);
    }

    @Test
    void isExistTvShowEpisodeWithTheMovieDbId_false() {
        //Arrest
        Long theMovieDbId = 0L;
        //Act
        boolean result = tvShowEpisodeRepository.isExistTvShowEpisodeWithTheMovieDbId(theMovieDbId);
        //Assert
        assertFalse(result);
    }
}