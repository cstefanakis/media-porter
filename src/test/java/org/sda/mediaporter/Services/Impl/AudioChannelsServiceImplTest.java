package org.sda.mediaporter.Services.Impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sda.mediaporter.Services.AudioChannelService;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class AudioChannelsServiceImplTest {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private AudioChannelRepository audioChannelRepository;

    @Autowired
    private AudioChannelService audioChannelService;

    @Test
    void getAudioChannelByChannels() {
    }

    @Test
    void getAllAudioChannels() {
    }

    @Test
    void getAudioChannelById() {
    }

    @Test
    void createAudioChannel() {
    }

    @Test
    void deleteAudioChannelById() {
    }

    @Test
    void updateAudioChannelById() {
    }
}