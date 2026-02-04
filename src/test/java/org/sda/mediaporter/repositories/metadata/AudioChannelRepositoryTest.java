package org.sda.mediaporter.repositories.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.metadata.AudioChannel;
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
class AudioChannelRepositoryTest {

    @Autowired
    private AudioChannelRepository audioChannelRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private AudioChannel mono;
    private AudioChannel stereo;

    @BeforeEach
    void loadData(){
        this.mono = testDataFactory.createAudioChannelMono();
        this.stereo = testDataFactory.createAudioChannelStereo();
    }

    @Test
    void findAudioChannelByChannel_fount() {
        //Arrest
        Integer channel = mono.getChannels();
        //Act
        Optional<AudioChannel> result = audioChannelRepository.findAudioChannelByChannel(channel);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findAudioChannelByChannel_notFount() {
        //Arrest
        Integer channel = 12;
        //Act
        Optional<AudioChannel> result = audioChannelRepository.findAudioChannelByChannel(channel);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAudioChannelByChannel_null() {
        //Act
        Optional<AudioChannel> result = audioChannelRepository.findAudioChannelByChannel(null);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAudioChannelByTitle_fount() {
        //Arrest
        String title = this.stereo.getTitle();
        //Act
        Optional<AudioChannel> result = audioChannelRepository.findAudioChannelByTitle(title);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findAudioChannelByTitle_notFount() {
        //Arrest
        String title = "noTitle";
        //Act
        Optional<AudioChannel> result = audioChannelRepository.findAudioChannelByTitle(title);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAudioChannelByTitle_null() {
        //Act
        Optional<AudioChannel> result = audioChannelRepository.findAudioChannelByTitle(null);
        //Assert
        assertFalse(result.isPresent());
    }
}