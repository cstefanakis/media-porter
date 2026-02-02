package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfigurationRepositoryTest {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private MovieRepository movieRepository;

    private SourcePath sourcePath;
    private Configuration configuration = new Configuration();
    private Genre action = new Genre();
    private Genre comedy = new Genre();

    @BeforeEach
    void setup(){
        movieRepository.save(Movie.builder()
                        .title("movie")
                        .originalTitle("originalMovieTitle")
                .theMovieDbId(100L)
                .lastModificationDateTime(LocalDateTime.now())
                .build());

        this.action = genreRepository.save(Genre.builder()
                        .title("Action")
                .build());

        this.comedy = genreRepository.save(Genre.builder()
                        .title("Comedy")
                .build());

        codecRepository.save(Codec.builder()
                        .name("AAC")
                        .mediaType(MediaTypes.AUDIO)
                .build());

        codecRepository.save(Codec.builder()
                .name("H264")
                .mediaType(MediaTypes.VIDEO)
                .build());

        resolutionRepository.save(Resolution.builder()
                        .name("720p")
                .build());

        resolutionRepository.save(Resolution.builder()
                .name("1080p")
                .build());


        this.configuration = configurationRepository.save(Configuration.builder()

                .maxDatesSaveFile(null)
                .maxDatesControlFilesFromExternalSource(5000)
                //Video
                .videoCodecs(null)
                .videoResolutions(new ArrayList<>())
                .firstVideoBitrateValueRange(2000)
                .secondVideoBitrateValueRange(5000)
                //Audio
                .audioCodecs(new ArrayList<>())
                .audioChannels(new ArrayList<>())
                .audioLanguages(new ArrayList<>())
                .firstAudioBitrateValueRange(128)
                .secondAudioBitrateValueRange(640)
                //Genres
                .genres(new ArrayList<>())
                //file size range
                .firstVideoSizeRange(null)
                .secondVideoSizeRange(null)
                .build());

        SourcePath sourcePath = sourcePathRepository.save(SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("C:\\Users\\chris\\Downloads\\Movies")
                .title("Movies Download Path")
                .pathType(SourcePath.PathType.DOWNLOAD)
                .build());

        configuration.setSourcePath(sourcePath);
        sourcePath.setConfiguration(configuration);
        this.sourcePath = sourcePathRepository.save(sourcePath);


    }

    @Test
    void isFileSupportSourceResolution() {
        //Arrest
        String resolution = "720p";
        //Act
        boolean result = configurationRepository.isFileSupportSourceResolution(resolution, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioCodecSupport() {
        //Arrest
        String audioCodec = "AAC";
        //Act
        boolean result = configurationRepository.isFileAudioCodecSupport(audioCodec, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportVideoCodec() {
        //Arrest
        String videoCodec = "H264";
        //Act
        boolean result = configurationRepository.isFileSupportVideoCodec(videoCodec, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileVideoBitrateInRange_bitrateIsNull() {
        //Arrest
        Integer videoBitrate = null;
        //Act
        boolean result = configurationRepository.isFileVideoBitrateInRange(videoBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileVideoBitrateInRangeBitrateIsInRange() {
        //Arrest
        Integer videoBitrate = 2500;
        //Act
        boolean result = configurationRepository.isFileVideoBitrateInRange(videoBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileVideoBitrateInRangeBitrateIsOutOfRange() {
        //Arrest
        Integer videoBitrate = 1000;
        //Act
        boolean result = configurationRepository.isFileVideoBitrateInRange(videoBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }


    @Test
    void isFileAudioChannelsSupport() {
        //Arrest
        Integer audioChannels = 5;
        //Act
        boolean result = configurationRepository.isFileAudioChannelsSupport(audioChannels, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioLanguageSupport() {
        //Arrest
        String audioLanguage = "EN";
        //Act
        boolean result = configurationRepository.isFileAudioLanguageSupport(audioLanguage, sourcePath);
        //Assert
        assertTrue(result);
    }


    @Test
    void isFileAudioBitrateInRange_toRangeStart() {
        //Arrest
        Integer audioBitrate = 128;
        //Act
        boolean result = configurationRepository.isFileAudioBitrateInRange(audioBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioBitrateInRange_toRangeEnd() {
        //Arrest
        Integer audioBitrate = 640;
        //Act
        boolean result = configurationRepository.isFileAudioBitrateInRange(audioBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioBitrateInRange_inRangeEnd() {
        //Arrest
        Integer audioBitrate = 320;
        //Act
        boolean result = configurationRepository.isFileAudioBitrateInRange(audioBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioBitrateInRange_outOfRangeEnd() {
        //Arrest
        Integer audioBitrate = 64;
        //Act
        boolean result = configurationRepository.isFileAudioBitrateInRange(audioBitrate, sourcePath);
        //Assert
        assertFalse(result);
    }

    @Test
    void isFileSupportGenres_configurationGenreIsEmpty() {
        //Arrest
        Genre genre = this.action;
        //Act
        boolean result = configurationRepository.isFileSupportGenres(genre, this.sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportGenres_configurationGenreIsIncludeGenre() {
        //Arrest
        Genre genre = this.action;
        this.configuration.setGenres(List.of(this.action, this.comedy));
        this.configuration = configurationRepository.save(this.configuration);
        //Act
        boolean result = configurationRepository.isFileSupportGenres(genre, this.sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportGenres_configurationGenreIsNotIncludeGenre() {
        //Arrest
        Genre genre = this.action;
        this.configuration.setGenres(List.of(this.comedy));
        this.configuration = configurationRepository.save(this.configuration);
        //Act
        boolean result = configurationRepository.isFileSupportGenres(genre, this.sourcePath);
        //Assert
        assertFalse(result);
    }

    @Test
    void findConfigurationBySourcePathId() {
        //Arrest
        Long sourcePathId = this.sourcePath.getId();
        Long configurationId = this.configuration.getId();
        //Act
        Optional<Configuration> result = configurationRepository.findConfigurationBySourcePathId(sourcePathId);
        //Assert
        assertTrue(result.isPresent());
        assertEquals(configurationId, result.get().getId());
    }
}