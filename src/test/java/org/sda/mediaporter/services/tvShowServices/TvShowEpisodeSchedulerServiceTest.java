package org.sda.mediaporter.services.tvShowServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.services.fileServices.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TvShowEpisodeSchedulerServiceTest {

    @Autowired
    private TvShowEpisodeSchedulerService tvShowEpisodeSchedulerService;

    @Autowired
    private FileService fileService;

    @Test
    void moveTvShowEpisodeFromDownloadsRootPathToMovieRootPath() {
        //Arrest
        Path pathFile = Path.of("src/test/resources/tvShows/Outer Banks (2020)/Season 01/Outer Banks (2020) - S01E01 - Pilot  (1080p H264) ([2 ENG]).mp4").normalize();
        //Act
        tvShowEpisodeSchedulerService.moveTvShowEpisodeFromDownloadsRootPathToTvShowsRootPath();
        //Assert
        assertTrue(Files.exists(pathFile));
        fileService.deleteFile(pathFile);
        fileService.deleteSubDirectories(pathFile);
    }

    @Test
    void scanTvShowSourcePath() {
        //
    }

    @Test
    void deleteTvShowEpisodesOlderThan() {
    }

    @Test
    void copyTvShowsEpisodesFromExternalSources() {
        //Act
        tvShowEpisodeSchedulerService.copyTvShowsEpisodesFromExternalSources();

    }
}