package org.sda.mediaporter.services.audioServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.dtos.AudioDto;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.services.CodecService;
import org.sda.mediaporter.services.LanguageService;
import org.sda.mediaporter.services.audioServices.impl.AudioServiceImpl;
import org.sda.mediaporter.services.fileServices.FileService;

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

    @Mock
    private FileService fileService;

    @Mock
    private LanguageService languageService;

    @InjectMocks
    private AudioServiceImpl audioService;

    private AudioChannel audioChannel;
    private Codec audioCodec;
    private Language english;
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
        this.english = Language.builder()
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
        when(fileService.runCommand(any(String[].class))).thenReturn("aac,2,128000,eng");
        when(languageService.getLanguageByCodeOrTitle(any(String.class))).thenReturn(this.english);
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
        // Arrange
        Path filePath = Path.of("file.mp4");
        AudioChannel stereo = AudioChannel.builder()
                .channels(2)
                .build();

        Codec aac = Codec.builder()
                .name("Aac")
                .build();

        AudioServiceImpl spyService = Mockito.spy(audioService);

        when(spyService.audioInfo(filePath))
                .thenReturn("aac,2,128000,eng");

        when(audioChannelService.getAudioChannelByChannelsOrNull(2)).thenReturn(stereo);
        when(codecService.getOrCreateCodecByCodecNameAndMediaType("Aac", MediaTypes.AUDIO)).thenReturn(aac);

        // Act
        List<Audio> result =
                spyService.getAudiosFromPathFile(filePath, videoFilePath);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        Audio audio = result.get(0);
        assertEquals("aac", audio.getCodec().getName().toLowerCase());
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
        Path filePath = Path.of("file.mp4").normalize();
        AudioServiceImpl spyService = Mockito.spy(audioService);
        when(spyService.audioInfo(filePath))
                .thenReturn("aac,2,128000,eng");
        String codec = "aac";
        Integer channels = 2;
        //Act
        List<AudioDto> result = spyService.getAudiosDetails(filePath);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(dto -> dto.getAudioCodec().equalsIgnoreCase(codec)));
        assertTrue(result.stream().anyMatch(dto -> dto.getAudioBitrate() == 128000));
        assertTrue(result.stream().anyMatch(dto -> dto.getAudioChannel().equals(channels)));
        assertTrue(result.stream().anyMatch(dto -> dto.getAudioLanguage().equalsIgnoreCase("eng")));
    }
}