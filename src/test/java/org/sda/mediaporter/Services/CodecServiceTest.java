package org.sda.mediaporter.Services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.CodecDto;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class CodecServiceTest {

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private SubtitleRepository subtitleRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private CodecService codecService;

    private Codec h264;
    private Codec mp3;
    private Codec subrip;

    @BeforeEach
    void setup(){
        configurationRepository.deleteAll();
        subtitleRepository.deleteAll();
        videoRepository.deleteAll();
        audioRepository.deleteAll();
        codecRepository.deleteAll();

        h264 = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.VIDEO)
                .name("H264")
                .build());

        mp3 = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("MP3")
                .build());

        subrip = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.SUBTITLE)
                .name("subrip")
                .build());
    }

    @Test
    void getCodecByNameAndMediaType_videCodecH264() {
        //Arrest
        String codecName = "h264";
        MediaTypes mediaType = MediaTypes.VIDEO;

        //Act
        Codec codec = codecService.getCodecByNameAndMediaType(codecName, mediaType);

        //Assert
        assertEquals("H264", codec.getName());
    }

    @Test
    void getCodecByNameAndMediaType_audioCodecMp3() {
        //Arrest
        String codecName = "subrip";
        MediaTypes mediaType = MediaTypes.SUBTITLE;

        //Act
        Codec codec = codecService.getCodecByNameAndMediaType(codecName, mediaType);

        //Assert
        assertEquals("subrip", codec.getName());
    }

    @Test
    void getCodecByNameAndMediaType_subtitleCodecSubrip() {
        //Arrest
        String codecName = "mp3";
        MediaTypes mediaType = MediaTypes.AUDIO;

        //Act
        Codec codec = codecService.getCodecByNameAndMediaType(codecName, mediaType);

        //Assert
        assertEquals("MP3", codec.getName());
    }

    @Test
    void getCodecByNameAndMediaType_notFoundCodec() {
        //Arrest
        String codecName = "aac";
        MediaTypes mediaType = MediaTypes.VIDEO;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> codecService.getCodecByNameAndMediaType(codecName, mediaType));
    }

    @Test
    void getCodecById() {
        //Arrest
        Long h264Id = h264.getId();

        //Act
        Codec codec = codecService.getCodecById(h264Id);

        //Assert
        assertEquals("H264", codec.getName());
    }

    @Test
    void getCodecById_notExistId() {
        //Arrest
        Long notExistId = 0L;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> codecService.getCodecById(notExistId));
    }

    @Test
    void getCodecByName() {
        //Arrest
        String codecName = "mp3";

        //Act
        Codec codec = codecService.getCodecByName(codecName);

        //Assert
        assertEquals("mp3", codec.getName().toLowerCase());
    }

    @Test
    void getCodecByName_notFoundCodec() {
        //Arrest
        String codecName = "acc";

        //Assert and name
        assertThrows(EntityNotFoundException.class, () -> codecService.getCodecByName(codecName));
    }

    @Test
    void getAllCodecs() {
        //Act
        List<Codec> codecs = codecService.getAllCodecs();

        //Assert
        assertNotNull(codecs);
        assertEquals(3, codecs.size());
    }

    @Test
    void getByMediaType_successfully() {
        //Arrest
        MediaTypes mediaType = MediaTypes.VIDEO;

        //Act
        List<Codec> videoCodecs = codecService.getByMediaType(mediaType);

        //Assert
        assertNotNull(videoCodecs);
        assertEquals(1, videoCodecs.size());
    }

    @Test
    void createCodec_withSameCodecName() {
        //Arrest
        CodecDto codecDto = CodecDto.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("h264")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, ()-> codecService.createCodec(codecDto));
    }

    @Test
    void createCodec_withNullName() {
        //Arrest
        CodecDto codecDto = CodecDto.builder()
                .mediaType(MediaTypes.AUDIO)
                .build();

        //Assert and Act
        assertThrows(ConstraintViolationException.class, ()-> codecService.createCodec(codecDto));
    }

    @Test
    void createCodec_withNullMediaType() {
        //Arrest
        CodecDto codecDto = CodecDto.builder()
                .name("h264")
                .build();

        //Assert and Act
        assertThrows(RuntimeException.class, ()-> codecService.createCodec(codecDto));
    }

    @Test
    void createCodec_successfully() {
        //Arrest
        CodecDto codecDto = CodecDto.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("DTS")
                .build();

        //Act
        Codec newCodec = codecService.createCodec(codecDto);

        //Assert
        assertNotNull(newCodec.getId());
    }



    @Test
    void updateCodec_withAllNullParameters() {
        //Arrest
        Long codecId = h264.getId();
        CodecDto codecDto = CodecDto.builder()
                .build();

        //Act
        codecService.updateCodec(codecId, codecDto);
        Codec updatedCodec = codecService.getCodecById(codecId);

        //Assert
        assertEquals("h264", updatedCodec.getName().toLowerCase());
        assertEquals(MediaTypes.VIDEO, updatedCodec.getMediaType());

    }

    @Test
    void updateCodec_withExistNameParameters() {
        //Arrest
        Long codecId = h264.getId();
        CodecDto codecDto = CodecDto.builder()
                .name("h264")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> codecService.updateCodec(codecId, codecDto));
    }

    @Test
    void updateCodec_successfully() {
        //Arrest
        Long codecId = subrip.getId();
        CodecDto codecDto = CodecDto.builder()
                .name("acc")
                .mediaType(MediaTypes.AUDIO)
                .build();

        //Act
        codecService.updateCodec(codecId, codecDto);
        Codec codec = codecService.getCodecById(codecId);

        //Assert
        assertEquals("acc", codec.getName().toLowerCase());
        assertEquals(MediaTypes.AUDIO, codec.getMediaType());
    }

    @Test
    void deleteCodec_notExistId() {
        //Arrest
        Long codecId = 0L;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, ()-> codecService.deleteCodec(codecId));
    }

    @Test
    void deleteCodec_successfully() {
        //Arrest
        Long codecId = mp3.getId();

        //Act
        codecService.deleteCodec(codecId);
        //Assert and Act
        assertTrue(codecRepository.findById(codecId).isEmpty());
    }

}