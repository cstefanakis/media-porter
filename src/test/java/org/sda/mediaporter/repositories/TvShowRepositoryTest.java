package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.TvShow;
import org.sda.mediaporter.models.TvShowEpisode;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class TvShowRepositoryTest {

    @Autowired
    private TvShowRepository tvShowRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private TvShow tvShow;
    private TvShow tvShowWithoutEpisodes;
    private SourcePath sourcePath1;
    private VideoFilePath videoFilePath1;


    @BeforeEach
    void setup(){
        this.videoFilePath1 = testDataFactory.createVideoFilePath();
        TvShowEpisode tvShowEpisode = this.videoFilePath1.getTvShowEpisode();
        this.tvShow = tvShowEpisode.getTvShow();
        this.sourcePath1 = this.videoFilePath1.getSourcePath();
        this.tvShowWithoutEpisodes = testDataFactory.createTvShowWithoutVideoFilePaths();
    }
    @Test
    void findTvShowsByTitle_TwoResults() {
        //Arrest
        String title = "title";
        Pageable pageable = PageRequest.of(0,5);
        //Act
        Page <TvShow> result = tvShowRepository.findTvShowsByTitle(title, pageable);
        //Assert
        assertEquals(2, result.getContent().size());
    }

    @Test
    void findTvShowsByTitle_OneResult() {
        //Arrest
        String title = "2";
        Pageable pageable = PageRequest.of(0,5);
        //Act
        Page <TvShow> result = tvShowRepository.findTvShowsByTitle(title, pageable);
        //Assert
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findTvShowsByTitle_NoResult() {
        //Arrest
        String title = "movie";
        Pageable pageable = PageRequest.of(0,5);
        //Act
        Page <TvShow> result = tvShowRepository.findTvShowsByTitle(title, pageable);
        //Assert
        assertEquals(0, result.getContent().size());
    }

    @Test
    void findTvShowByTheMovieDBId_findIt() {
        //Arrest
        Long tvShowTMDBId = tvShow.getTheMoveDBTvShowId();
        //Act
        Optional<TvShow> result = tvShowRepository.findTvShowByTheMovieDBId(tvShowTMDBId);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findTvShowByTheMovieDBId_NoFindIt() {
        //Arrest
        Long tvShowTMDBId = 0L;
        //Act
        Optional<TvShow> result = tvShowRepository.findTvShowByTheMovieDBId(tvShowTMDBId);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath_3DaysOld() {
        //Arrest
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(3);
        //Act
        List<Long> result = tvShowRepository.findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(localDateTime, this.sourcePath1);
        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath_1aysOld() {
        //Arrest
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
        //Act
        List<Long> result = tvShowRepository.findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(localDateTime, this.sourcePath1);
        //Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findTvShowIdByVideoFilePathId() {
        //Arrest
        Long videoFilePathId = this.videoFilePath1.getId();
        Long tvShowId = this.tvShow.getId();
        //Act
        Long result = tvShowRepository.findTvShowIdByVideoFilePathId(videoFilePathId);
        //Assert
        assertEquals(tvShowId, result);
    }

    @Test
    void deleteTvShowWithoutTvShowEpisodes() {
        //Arrest
        Long tvShowId = this.tvShowWithoutEpisodes.getId();
        //Act
        tvShowRepository.deleteTvShowWithoutTvShowEpisodes(tvShowId);
        Optional <TvShow> result = tvShowRepository.findById(tvShowId);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void deleteTvShowsWithoutTvShowEpisodes() {
        //Arrest
        Long tvShowWithoutTvShowEpisodeId = this.tvShowWithoutEpisodes.getId();
        Optional<TvShow> tvShowOptional = tvShowRepository.findById(tvShowWithoutTvShowEpisodeId);
        assertTrue(tvShowOptional.isPresent());
        //Act
        tvShowRepository.deleteTvShowsWithoutTvShowEpisodes();
        Optional<TvShow> result = tvShowRepository.findById(tvShowWithoutTvShowEpisodeId);
        //Assert
        assertFalse(result.isPresent());
    }
}