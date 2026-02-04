package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class ConfigurationRepositoryTest {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Genre action;
    private Genre adventure;
    private Configuration tvShowConfig;
    private SourcePath tvShowSP;

    @BeforeEach
    void setup(){
        VideoFilePath tvShowVfp = testDataFactory.createTvShowVideoFilePath();
        this.action = testDataFactory.createGenreAction();
        this.tvShowSP = tvShowVfp.getSourcePath();
        this.tvShowConfig = this.tvShowSP.getConfiguration();
        this.adventure = testDataFactory.createGenreAdventure();
    }

    @Test
    void isFileSupportSourceResolution() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        String resolution = "720p";
        //Act
        boolean result = configurationRepository.isFileSupportSourceResolution(resolution, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioCodecSupport() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        String audioCodec = "AAC";
        //Act
        boolean result = configurationRepository.isFileAudioCodecSupport(audioCodec, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportVideoCodec() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        String videoCodec = "H264";
        //Act
        boolean result = configurationRepository.isFileSupportVideoCodec(videoCodec, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileVideoBitrateInRange_bitrateIsNull() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        Integer videoBitrate = null;
        //Act
        boolean result = configurationRepository.isFileVideoBitrateInRange(videoBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileVideoBitrateInRangeBitrateIsInRange() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        Integer videoBitrate = 2500;
        //Act
        boolean result = configurationRepository.isFileVideoBitrateInRange(videoBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileVideoBitrateInRangeBitrateIsOutOfRange() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        Integer videoBitrate = 1000;
        //Act
        boolean result = configurationRepository.isFileVideoBitrateInRange(videoBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }


    @Test
    void isFileAudioChannelsSupport() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        Integer audioChannels = 5;
        //Act
        boolean result = configurationRepository.isFileAudioChannelsSupport(audioChannels, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioLanguageSupport() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        String audioLanguage = "EN";
        //Act
        boolean result = configurationRepository.isFileAudioLanguageSupport(audioLanguage, sourcePath);
        //Assert
        assertTrue(result);
    }


    @Test
    void isFileAudioBitrateInRange_toRangeStart() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        Integer audioBitrate = 128;
        //Act
        boolean result = configurationRepository.isFileAudioBitrateInRange(audioBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioBitrateInRange_toRangeEnd() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        Integer audioBitrate = 640;
        //Act
        boolean result = configurationRepository.isFileAudioBitrateInRange(audioBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioBitrateInRange_inRangeEnd() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        Integer audioBitrate = 320;
        //Act
        boolean result = configurationRepository.isFileAudioBitrateInRange(audioBitrate, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileAudioBitrateInRange_outOfRangeEnd() {
        //Arrest
        SourcePath sourcePath = this.tvShowSP;
        Integer audioBitrate = 64;
        //Act
        boolean result = configurationRepository.isFileAudioBitrateInRange(audioBitrate, sourcePath);
        //Assert
        assertFalse(result);
    }

    @Test
    void isFileSupportGenres_configurationGenreIsEmpty() {
        //Arrest

        SourcePath sourcePath = this.tvShowSP;
        Genre genre = this.action;
        //Act
        boolean result = configurationRepository.isFileSupportGenres(genre, sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportGenres_configurationGenreIsIncludeGenre() {
        //Arrest
        this.tvShowConfig.getGenres().add(this.action);
        this.tvShowConfig.getGenres().add(this.adventure);
        this.tvShowConfig = configurationRepository.save(this.tvShowConfig);
        //Act
        boolean result = configurationRepository.isFileSupportGenres(this.action, this.tvShowSP);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportGenres_configurationGenreIsNotIncludeGenre() {
        //Arrest
        Genre genre = this.action;
        this.tvShowConfig.setGenres(List.of(this.adventure));
        this.tvShowConfig = configurationRepository.save(this.tvShowConfig);
        //Act
        boolean result = configurationRepository.isFileSupportGenres(genre, this.tvShowSP);
        //Assert
        assertFalse(result);
    }

    @Test
    void findConfigurationBySourcePathId() {
        //Arrest
        Long sourcePathId = this.tvShowSP.getId();
        Long configurationId = this.tvShowConfig.getId();
        //Act
        Optional<Configuration> result = configurationRepository.findConfigurationBySourcePathId(sourcePathId);
        //Assert
        assertTrue(result.isPresent());
        assertEquals(configurationId, result.get().getId());
    }
}