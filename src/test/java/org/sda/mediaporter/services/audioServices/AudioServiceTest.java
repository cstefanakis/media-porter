package org.sda.mediaporter.services.audioServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.services.CodecService;
import org.sda.mediaporter.services.audioServices.impl.AudioServiceImpl;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudioServiceTest {

    @Mock
    private AudioRepository audioRepository;

    @Mock
    private AudioChannelService audioChannelService;

    @Mock
    private CodecService codecService;

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private AudioServiceImpl audioService;

    private Audio audio;
    private AudioChannel audioChannel;
    private Codec videoCodec;
    private Language language;
    private Codec audioCodec;

    @BeforeEach
    void loadData(){
        this.audioChannel = AudioChannel.builder()
                .channels(2)
                .title("Stereo")
                .build();

        this.videoCodec = Codec.builder()
                .name("H264")
                .mediaType(MediaTypes.VIDEO)
                .build();

        this.audioCodec = Codec.builder()
                .name("AAC")
                .mediaType(MediaTypes.VIDEO)
                .build();

        this.language = Language.builder()
                .englishTitle("English")
                .iso6391("En")
                .iso6392B("Eng")
                .iso6392T("Eng")
                .build();

        this.audio = Audio.builder()
                .audioChannel(audioChannel)
                .codec(audioCodec)
                .language(language)
                .bitrate(128)
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
        Path path = Path.of("src/test/resources/files/movies/Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4").normalize();
        VideoFilePath videoFilePath = new VideoFilePath();
        //Act
        List<Audio> result = audioService.getCreatedAudiosFromPathFile(path, videoFilePath);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(a -> a.getAudioChannel().getChannels() == 2));
        assertTrue(result.stream().anyMatch(a -> a.getCodec().getName().equalsIgnoreCase(audioCodec)));
    }

    @Test
    void getAudiosFromPathFile() {

    }

    @Test
    void deleteAudioById() {
    }

    @Test
    void getAudiosDetails() {
    }
}