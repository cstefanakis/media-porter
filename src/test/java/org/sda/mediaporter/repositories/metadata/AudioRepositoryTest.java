package org.sda.mediaporter.repositories.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.metadata.Audio;
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
class AudioRepositoryTest {

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Audio audio;

    @BeforeEach
    void loadData(){
        this.audio = testDataFactory.createTvShowAudio();
    }

    @Test
    void findById(){
        //Arrest
        Long audioId = this.audio.getId();
        //Act
        Optional<Audio> result = audioRepository.findById(audioId);
        //Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
    }
}