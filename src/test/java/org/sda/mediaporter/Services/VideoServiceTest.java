package org.sda.mediaporter.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class VideoServiceTest {

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoService videoService;

    private final Path moviePath = Path.of("src/test/resources/movies/Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4");
    private final Path textPath = Path.of("src/test/resources/files/test.txt");

    @BeforeEach
    void setup(){
        videoRepository.deleteAll();
        movieRepository.deleteAll();
        configurationRepository.deleteAll();
        resolutionRepository.deleteAll();
        codecRepository.deleteAll();
    }

    @Test
    void getCodecFromFilePathViFFMpeg_withData() {
        //Act
        String codec = videoService.getCodecFromFilePathViFFMpeg(this.moviePath);

        //assert
        assertEquals("h264", codec);
    }

    @Test
    void getCodecFromFilePathViFFMpeg_withNoData() {
        //Act
        String codec = videoService.getCodecFromFilePathViFFMpeg(this.textPath);

        //assert
        assertNull(codec);
    }

    @Test
    void getResolutionFromFilePathViFFMpeg_withData() {
        //Act
        String resolution = videoService.getResolutionFromFilePathViFFMpeg(this.moviePath);

        //assert
        assertEquals("720P", resolution);
    }

    @Test
    void getResolutionFromFilePathViFFMpeg_withNoData() {
        //Act
        String resolution = videoService.getResolutionFromFilePathViFFMpeg(this.textPath);

        //assert
        assertNull(resolution);
    }

    @Test
    void getBitrateFromFilePathViFFMpeg_withData() {
        //Act
        Integer bitrate = videoService.getBitrateFromFilePathViFFMpeg(this.moviePath);

        //assert
        assertNull(bitrate);
    }

    @Test
    void getBitrateFromFilePathViFFMpeg_withNoData() {
        //Act
        Integer bitrate = videoService.getBitrateFromFilePathViFFMpeg(this.textPath);

        //assert
        assertNull(bitrate);
    }

    @Test
    void getVideoFromPath() {

        //Act
        Video video = videoService.getVideoFromPath(this.moviePath);

        //Assert
        assertNotNull(video);
        assertNotNull(video.getResolution());
        assertEquals(MediaTypes.VIDEO, video.getCodec().getMediaType());
    }

    @Test
    void createVideoFromPath() {
        //Arrest
        Movie movie = movieRepository.save(Movie.builder()
                .title("Thor")
                .year(2011)
                .build());

        //Act
        Video createdVideo = videoService.createVideoFromPath(this.moviePath, movie);

        //Assert
        assertNotNull(createdVideo);
        assertNotNull(createdVideo.getResolution());
        assertEquals(MediaTypes.VIDEO, createdVideo.getCodec().getMediaType());
    }
}