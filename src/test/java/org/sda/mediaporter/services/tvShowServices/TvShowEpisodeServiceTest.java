package org.sda.mediaporter.services.tvShowServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowEpisodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TvShowEpisodeServiceTest {

    @Autowired
    private TvShowEpisodeService tvShowEpisodeService;
}