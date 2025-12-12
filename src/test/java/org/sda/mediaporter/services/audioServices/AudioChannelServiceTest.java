package org.sda.mediaporter.services.audioServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.services.audioServices.impl.AudioChannelsServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudioChannelServiceTest {

    @Mock
    private AudioChannelRepository audioChannelRepository;

    @InjectMocks
    private AudioChannelsServiceImpl audioChannelsService;

    @Test
    void getAudioChannelByChannels() {
        //Arrange
        Integer channels = 6;
        AudioChannel audioChannel = new AudioChannel();
        audioChannel.setChannels(channels);

        when(audioChannelRepository.findAudioChannelByChannel(channels))
                .thenReturn(Optional.of(audioChannel));
        //Act
        AudioChannel result = audioChannelsService.getAudioChannelByChannels(channels);
        //Assert
        assertNotNull(result);
        assertEquals(channels, result.getChannels());
        verify(audioChannelRepository).findAudioChannelByChannel(channels);
    }

    @Test
    void getAllAudioChannels() {
        //Arrange
        AudioChannel stereo = AudioChannel.builder()
                .channels(2)
                .title("Stereo")
                .build();
        AudioChannel mono = AudioChannel.builder()
                .channels(1)
                .title("Mono")
                .build();
        List<AudioChannel> audioChannelList = List.of(stereo, mono);

        when(audioChannelRepository.findAll())
                .thenReturn(audioChannelList);

        //Act
        List<AudioChannel> result = audioChannelsService.getAllAudioChannels();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(audioChannelRepository).findAll();
    }

    @Test
    void getAudioChannelById() {
        //Arrest
        Long id = 1L;
        AudioChannel mono = AudioChannel.builder()
                .title("Mono")
                .channels(1)
                .build();
        when(audioChannelRepository.findById(id))
                .thenReturn(Optional.of(mono));

        //Act
        AudioChannel result = audioChannelsService.getAudioChannelById(id);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.getChannels());
        assertEquals("Mono", result.getTitle());
        verify(audioChannelRepository).findById(id);

    }

    @Test
    void getAudioChannelByChannelsOrNull_null() {
        //Arrest
        Integer channel = 1;
        when(audioChannelRepository.findAudioChannelByChannel(channel))
                .thenReturn(Optional.empty());

        //Act
        AudioChannel result = audioChannelsService.getAudioChannelByChannelsOrNull(channel);
        //Assert
        assertNull(result);
        verify(audioChannelRepository).findAudioChannelByChannel(channel);
    }

    @Test
    void getAudioChannelByChannelsOrNull() {
        //Arrest
        Integer channel = 1;
        AudioChannel mono = AudioChannel.builder()
                .title("Mono")
                .channels(1)
                .build();
        when(audioChannelRepository.findAudioChannelByChannel(channel))
                .thenReturn(Optional.of(mono));

        //Act
        AudioChannel result = audioChannelsService.getAudioChannelByChannelsOrNull(1);
        //Assert
        assertNotNull(result);
        assertEquals(1, result.getChannels());
        assertEquals("Mono", result.getTitle());
        verify(audioChannelRepository).findAudioChannelByChannel(1);
    }
}