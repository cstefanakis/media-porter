package org.sda.mediaporter.services.audioServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.dtos.AudioDto;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.services.CodecService;
import org.sda.mediaporter.services.audioServices.impl.AudioServiceImpl;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AudioServiceTest {

    @Mock
    private AudioRepository audioRepository;

    @Mock
    private AudioChannelService audioChannelService;

    @Mock
    private CodecService codecService;

    @InjectMocks
    private AudioServiceImpl audioService;

    private AudioChannel audioChannel;
    private Codec audioCodec;
    private final Path moviePath = Path.of("src/test/resources/files/movies/Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4").normalize();
    private VideoFilePath videoFilePath;

    @BeforeEach
    void loadData(){
        this.audioChannel = AudioChannel.builder()
                .channels(2)
                .title("Stereo")
                .build();

        this.audioCodec = Codec.builder()
                .name("AAC")
                .mediaType(MediaTypes.VIDEO)
                .build();

        this.videoFilePath = VideoFilePath.builder()
                .filePath(this.moviePath.toString())
                .build();
    }

    @Test
    void getCreatedAudiosFromPathFile() {
        //Arrest
        String audioCodec = "Aac";
        when(audioChannelService.getAudioChannelByChannelsOrNull(2)).thenReturn(this.audioChannel);
        when(codecService.getOrCreateCodecByCodecNameAndMediaType(audioCodec, MediaTypes.AUDIO)).thenReturn(this.audioCodec);
        when(audioRepository.save(any(Audio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        List<Audio> result = audioService.getCreatedAudiosFromPathFile(this.moviePath, this.videoFilePath);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(a -> a.getAudioChannel().getChannels() == 2));
        assertTrue(result.stream().anyMatch(a -> a.getCodec().getName().equalsIgnoreCase(audioCodec)));
        verify(audioChannelService, times(1)).getAudioChannelByChannelsOrNull(2);
        verify(codecService, times(1)).getOrCreateCodecByCodecNameAndMediaType(audioCodec, MediaTypes.AUDIO);
    }

    @Test
    void getAudiosFromPathFile() {
        //Arrest
        String audioCodec = "Aac";
        when(audioChannelService.getAudioChannelByChannelsOrNull(2)).thenReturn(this.audioChannel);
        when(codecService.getOrCreateCodecByCodecNameAndMediaType(audioCodec, MediaTypes.AUDIO)).thenReturn(this.audioCodec);
        //Act
        List<Audio> result = audioService.getAudiosFromPathFile(this.moviePath, this.videoFilePath);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(a -> a.getAudioChannel().getChannels() == 2));
        assertTrue(result.stream().anyMatch(a -> a.getCodec().getName().equalsIgnoreCase(audioCodec)));
        verify(audioChannelService, times(1)).getAudioChannelByChannelsOrNull(2);
        verify(codecService, times(1)).getOrCreateCodecByCodecNameAndMediaType(audioCodec, MediaTypes.AUDIO);
    }

    @Test
    void deleteAudioById() {
        //Arrest
        Long audioId = 1L;
        doNothing().when(audioRepository).deleteById(audioId);
        //Act
        audioService.deleteAudioById(audioId);
        //Assert
        verify(audioRepository, times(1)).deleteById(audioId);
    }

    @Test
    void getAudiosDetails() {
        //Arrest
        String codec = "aac";
        Integer channels = 2;
        Integer bitrate= null;
        //Act
        List<AudioDto> result = audioService.getAudiosDetails(this.moviePath);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(dto -> dto.getAudioCodec().equalsIgnoreCase(codec)));
        assertTrue(result.stream().anyMatch(dto -> dto.getAudioBitrate() == null));
        assertTrue(result.stream().anyMatch(dto -> dto.getAudioChannel().equals(channels)));
        assertTrue(result.stream().anyMatch(dto -> dto.getAudioLanguage() == null));
    }
}