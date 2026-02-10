package org.sda.mediaporter.services.tvShowServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.services.fileServices.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TvShowEpisodeSchedulerServiceTest {

    @Autowired
    private TvShowEpisodeSchedulerService tvShowEpisodeSchedulerService;

    @Autowired
    private FileService fileService;

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

    }
}