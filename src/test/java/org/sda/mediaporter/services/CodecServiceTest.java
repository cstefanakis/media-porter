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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        //Arrest
        Long id = 1L;
        when(codecRepository.findById(id))
                .thenReturn(Optional.of(this.h265));
        //Act
        Codec result = codecService.getCodecById(id);
        //Assert
        assertNotNull(result);
        assertEquals("H265", result.getName());
        verify(codecRepository).findById(id);
    }

    @Test
    void getCodecByName() {
        //Arrest
        String name = "H264";
        when(codecRepository.findByName(name))
                .thenReturn(Optional.of(this.h264));
        //Act
        Codec result = codecService.getCodecByName(name);
        //Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(codecRepository).findByName(name);
    }

    @Test
    void getAllCodecs() {
        //Arrest
        List<Codec> codecs = List.of(this.h264, this.h265, this.mp3, this.aac,this.srt,this.sub);
        when(codecRepository.findAll())
                .thenReturn(codecs);
        //Act
        List<Codec> result = codecService.getAllCodecs();
        //Assert
        assertNotNull(result);
        assertEquals(6 ,result.size());
        verify(codecRepository).findAll();
    }

    @Test
    void getCodecsByMediaType() {
        //Arrest
        MediaTypes mediaTypes = MediaTypes.VIDEO;
        List<Codec> codecs = List.of(this.h264, this.h265);
        when(codecRepository.findByMediaType(mediaTypes))
                .thenReturn(codecs);
        //Act
        List<Codec> result = codecService.getCodecsByMediaType(mediaTypes);
        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(codecRepository).findByMediaType(mediaTypes);
    }

    @Test
    void getOrCreateCodecByCodecNameAndMediaType() {
        //Arrest
        MediaTypes mediaType = MediaTypes.VIDEO;
        String name = "h264";
        when(codecRepository.findByNameAndMediaType(name, mediaType))
                .thenReturn(Optional.empty());
        when(codecRepository.save(any(Codec.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        Codec result = codecService.getOrCreateCodecByCodecNameAndMediaType(name, mediaType);
        //Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(codecRepository).findByNameAndMediaType(name, mediaType);
        verify(codecRepository).save(any(Codec.class));
    }
}