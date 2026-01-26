package org.sda.mediaporter.services.audioServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.AudioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AudioServiceTest {

    @Autowired
    private AudioService audioService;

    @Test
    void getAudiosDetails() {
        //Arrest
        Path filePath = Path.of("src\\test\\resources\\files\\Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4");
        //Act
        List<AudioDto> result = audioService.getAudiosDetails(filePath);
        //Assert
        assertFalse(result.isEmpty());
        assertNotNull(result);
        assertEquals("Aac" , result.getFirst().getAudioCodec());
        assertNull(result.getFirst().getAudioBitrate());
        assertEquals(2 , result.getFirst().getAudioChannel());
        assertNull(result.getFirst().getAudioLanguage());
    }
}