package org.sda.mediaporter.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.services.impl.CodecServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CodecServiceTest {

    @Mock
    CodecRepository codecRepository;

    @InjectMocks
    CodecServiceImpl codecService;

    private Codec h264;
    private Codec h265;
    private Codec mp3;
    private Codec aac;
    private Codec srt;
    private Codec sub;

    @BeforeEach
    void setup(){
        h264 = Codec.builder()
                .mediaType(MediaTypes.VIDEO)
                .name("H264")
                .build();
        h265 = Codec.builder()
                .mediaType(MediaTypes.VIDEO)
                .name("H265")
                .build();
        mp3 = Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("MP3")
                .build();
        aac = Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("AAC")
                .build();
        srt = Codec.builder()
                .mediaType(MediaTypes.SUBTITLE)
                .name("SRT")
                .build();
        sub = Codec.builder()
                .mediaType(MediaTypes.SUBTITLE)
                .name("SUB")
                .build();
    }

    @Test
    void getCodecByNameAndMediaType() {
        //Arrest
        String codecName = "H264";
        MediaTypes mediaType = MediaTypes.VIDEO;
        when(codecRepository.findByNameAndMediaType(codecName, mediaType))
                .thenReturn(Optional.of(this.h264));
        //Act
        Codec result = codecService.getCodecByNameAndMediaType(codecName, mediaType);
        //Assert
        assertNotNull(result);
        assertEquals(codecName, result.getName());
        assertEquals(mediaType, result.getMediaType());
        verify(codecRepository).findByNameAndMediaType(codecName, mediaType);
    }

    @Test
    void getCodecById() {
    }

    @Test
    void getCodecByName() {
    }

    @Test
    void getAllCodecs() {
    }

    @Test
    void getCodecsByMediaType() {
    }

    @Test
    void getOrCreateCodecByCodecNameAndMediaType() {
    }
}