package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.Services.AudioChannelService;
import org.sda.mediaporter.dtos.AudioChannelDto;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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

    private AudioChannel stereo;
    private AudioChannel surround;

    @BeforeEach
    void setup(){
        configurationRepository.deleteAll();
        audioChannelRepository.deleteAll();
        audioChannelRepository.deleteAll();

        stereo = audioChannelRepository.save(AudioChannel.builder()
                .title("2 Stereo")
                .channels(2)
                .description("Two-channel stereo sound")
                .build()
        );

        surround = audioChannelRepository.save(
                AudioChannel.builder()
                        .title("5.1 Surround")
                        .channels(6)
                        .description("Six-channel surround (5 speakers + 1 sub)")
                        .build()
        );

    }

    @Test
    void getAudioChannelByChannels_successfully() {
        //Arrest
        int stereoChannel = 2;

        //Act
        AudioChannel stereo = audioChannelService.getAudioChannelByChannels(stereoChannel);

        //Assert
        assertEquals("2 Stereo", stereo.getTitle());
    }

    @Test
    void getAudioChannelByChannels_notExistChannel() {
        //Arrest
        int stereoChannel = 100;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> audioChannelService.getAudioChannelByChannels(stereoChannel));
    }

    @Test
    void getAllAudioChannels() {
        //Act
        List<AudioChannel> allAudioChannels = audioChannelService.getAllAudioChannels();

        //Assert
        assertEquals(2, allAudioChannels.size());
    }

    @Test
    void getAudioChannelById_successfully() {
        //Arrest
        Long stereoId = stereo.getId();

        //Act
        AudioChannel stereoAudioChannel = audioChannelService.getAudioChannelById(stereoId);

        //Assert
        assertEquals("2 Stereo", stereo.getTitle());
    }

    @Test
    void getAudioChannelById_notIdFound() {
        //Arrest
        Long notExistId = 0L;

        //Act and Assert
        assertThrows(EntityNotFoundException.class, () -> audioChannelService.getAudioChannelById(notExistId));
    }

    @Test
    void createAudioChannel_withNullChannel() {
        //Arrest
        AudioChannelDto fullSurroundDto = AudioChannelDto.builder()
                .title("7.1 Full Surround")
                .description("Eight-channel surround")
                .build();

        //Act and Assert
        assertThrows(ConstraintViolationException.class, () -> audioChannelService.createAudioChannel(fullSurroundDto));
    }

    @Test
    void createAudioChannel_withNullTitle() {
        //Arrest
        AudioChannelDto fullSurroundDto = AudioChannelDto.builder()
                .channels(8)
                .description("Eight-channel surround")
                .build();

        //Act and Assert
        assertThrows(ConstraintViolationException.class, () -> audioChannelService.createAudioChannel(fullSurroundDto));
    }

    @Test
    void createAudioChannel_successfully() {
        //Arrest
        AudioChannelDto fullSurroundDto = AudioChannelDto.builder()
                .title("7.1 Full Surround")
                .channels(8)
                .description("Eight-channel surround")
                .build();

        //Act
        AudioChannel createdAudioChannel = audioChannelService.createAudioChannel(fullSurroundDto);

        //Assert
        assertNotNull(createdAudioChannel.getId());
        assertEquals("7.1 Full Surround", createdAudioChannel.getTitle());
        assertEquals(8, createdAudioChannel.getChannels());
        assertEquals("Eight-channel surround", createdAudioChannel.getDescription());
    }

    @Test
    void deleteAudioChannelById_notFoundId() {
        //Arrest
        Long notExistId = 0L;

        //Act and Assert
        assertThrows(EntityNotFoundException.class, () -> audioChannelService.deleteAudioChannelById(notExistId));

    }

    @Test
    void deleteAudioChannelById_successfully() {
        //Arrest
        Long surroundId = surround.getId();

        //Act
        audioChannelService.deleteAudioChannelById(surroundId);

        //Assert
        assertThrows(EntityNotFoundException.class, () -> audioChannelService.getAudioChannelById(surroundId));

    }

    @Test
    void updateAudioChannelById_withNullParameters() {
        //Arrest
        Long surroundId = surround.getId();
        AudioChannelDto fullSurroundDto = AudioChannelDto.builder()
                .build();

        //Act
        audioChannelService.updateAudioChannelById(surroundId, fullSurroundDto);
        AudioChannel surroundAudioChannel = audioChannelService.getAudioChannelById(surroundId);

        //Assert
        assertNotNull(surroundAudioChannel.getId());
        assertEquals("5.1 Surround", surroundAudioChannel.getTitle());
        assertEquals(6, surroundAudioChannel.getChannels());
        assertEquals("Six-channel surround (5 speakers + 1 sub)", surroundAudioChannel.getDescription());
    }

    @Test
    void updateAudioChannelById_withExistTitle() {
        //Arrest
        Long surroundId = surround.getId();
        AudioChannelDto fullSurroundDto = AudioChannelDto.builder()
                .title("2 Stereo")
                .channels(8)
                .description("Eight-channel surround")
                .build();

        //Act and Assert
        assertThrows(EntityExistsException.class, () -> audioChannelService.updateAudioChannelById(surroundId, fullSurroundDto));
    }

    @Test
    void updateAudioChannelById_withExistChannel() {
        //Arrest
        Long surroundId = surround.getId();
        AudioChannelDto fullSurroundDto = AudioChannelDto.builder()
                .title("7.1 Full Surround")
                .channels(2)
                .description("Eight-channel surround")
                .build();

        //Act and Assert
        assertThrows(EntityExistsException.class, () -> audioChannelService.updateAudioChannelById(surroundId, fullSurroundDto));
    }

    @Test
    void updateAudioChannelById_withSameParameters() {
        //Arrest
        Long surroundId = surround.getId();
        AudioChannelDto fullSurroundDto = AudioChannelDto.builder()
                .title("5.1 Surround")
                .channels(6)
                .description("Six-channel surround (5 speakers + 1 sub)")
                .build();

        //Act
        audioChannelService.updateAudioChannelById(surroundId, fullSurroundDto);
        AudioChannel surroundAudioChannel = audioChannelService.getAudioChannelById(surroundId);

        //Assert
        assertNotNull(surroundAudioChannel.getId());
        assertEquals("5.1 Surround", surroundAudioChannel.getTitle());
        assertEquals(6, surroundAudioChannel.getChannels());
        assertEquals("Six-channel surround (5 speakers + 1 sub)", surroundAudioChannel.getDescription());
    }

    @Test
    void updateAudioChannelById_successfully() {
        //Arrest
        Long surroundId = surround.getId();
        AudioChannelDto fullSurroundDto = AudioChannelDto.builder()
                .title("7.1 Full Surround")
                .channels(8)
                .description("Eight-channel surround")
                .build();

        //Act
        audioChannelService.updateAudioChannelById(surroundId, fullSurroundDto);
        AudioChannel surroundAudioChannel = audioChannelService.getAudioChannelById(surroundId);

        //Assert
        assertNotNull(surroundAudioChannel.getId());
        assertEquals("7.1 Full Surround", surroundAudioChannel.getTitle());
        assertEquals(8, surroundAudioChannel.getChannels());
        assertEquals("Eight-channel surround", surroundAudioChannel.getDescription());
    }
}