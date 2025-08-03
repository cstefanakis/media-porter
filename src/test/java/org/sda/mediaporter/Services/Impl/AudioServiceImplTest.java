package org.sda.mediaporter.Services.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.Services.AudioService;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class AudioServiceImplTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private AudioChannelRepository audioChannelRepository;

    @Autowired
    private AudioService audioService;

    @BeforeEach
    void setup(){
        movieRepository.deleteAll();
        configurationRepository.deleteAll();
        codecRepository.deleteAll();
        languageRepository.deleteAll();
        audioChannelRepository.deleteAll();
        audioRepository.deleteAll();

        Codec aac3Codec = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("EAC3")
                .build());


    }

    @Test
    void createAudioListFromFile() {

    }

    @Test
    void getAudioListFromFile() {
    }

    @Test
    void getAudioCodec() {
    }

    @Test
    void getAudioChannel() {
    }

    @Test
    void getAudioBitrate() {
    }

    @Test
    void getAudioLanguageByCode() {
    }
}