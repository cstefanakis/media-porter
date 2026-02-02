package org.sda.mediaporter.services.videoServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.VideoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VideoServiceTest {

    @Autowired
    private VideoService videoService;

//    @Test
//    void getVideoDetails() {
//        //Arrest
//        Path filePath = Path.of("src\\test\\resources\\files\\Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4").normalize();
//        //Act
//        VideoDto result = videoService.getVideoDetails(filePath);
//        //Assert
//        assertNotNull(result);
//        assertNull(result.getVideoBitrate());
//        assertEquals("720P", result.getVideoResolution());
//        assertEquals("h264", result.getVideoCodec());
//    }
}