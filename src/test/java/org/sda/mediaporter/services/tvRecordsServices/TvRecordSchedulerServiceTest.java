package org.sda.mediaporter.services.tvRecordsServices;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TvRecordSchedulerServiceTest {

    @Autowired
    private TvRecordSchedulerService tvRecordSchedulerService;

    @Test
    void copyMoviesFromTvShowSources() {
        //Act
        tvRecordSchedulerService.copyMoviesFromTvShowSources();

    }
}