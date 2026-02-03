package org.sda.mediaporter.repositories.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Video video;

    @BeforeEach
    void loadData(){
        this.video = testDataFactory.createTvShowVideo();
    }

    @Test
    void getVideoById(){
        //Arrest
        Long videoId = this.video.getId();
        //Act
        Optional<Video> result = videoRepository.findById(videoId);
        //Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
    }
}