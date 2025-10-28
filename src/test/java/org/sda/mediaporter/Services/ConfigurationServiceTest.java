package org.sda.mediaporter.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.ConfigurationDto;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.GenreRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class ConfigurationServiceTest {

    @Autowired
    private  ConfigurationRepository configurationRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private AudioChannelRepository audioChannelRepository;

    @Autowired
    private ConfigurationService configurationService;

    @BeforeEach
    void setup(){
        configurationRepository.deleteAll();
        genreRepository.deleteAll();
        resolutionRepository.deleteAll();
        codecRepository.deleteAll();
        languageRepository.deleteAll();
        audioChannelRepository.deleteAll();

        genreRepository.save(Genre.builder()
                .title("Action")
                .build());

        genreRepository.save(Genre.builder()
                .title("Adventure")
                .build());

        genreRepository.save(Genre.builder()
                .title("Animation")
                .build());

        codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.VIDEO)
                .name("H264")
                .build());

        codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("EAC3")
                .build());

        codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("AAC")
                .build());

        codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.SUBTITLE)
                .name("subrip")
                .build());

        languageRepository.save(Language.builder()
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .englishTitle("English")
                .originalTitle("English")
                .build());

        resolutionRepository.save(Resolution.builder()
                .name("720p")
                .build());

        audioChannelRepository.save(AudioChannel.builder()
                .title("2 Stereo")
                .channels(2)
                .description("Two-channel stereo sound")
                .build());

        audioChannelRepository.save(AudioChannel.builder()
                .title("5.1 Surround")
                .channels(6)
                .description("Six-channel surround (5 speakers + 1 sub)")
                .build());

        configurationRepository.save(Configuration.builder()
                .id(1L)
                .maxDatesSaveFile(9000)
                .maxDatesControlFilesFromExternalSource(0)
                .videoResolutions(resolutionRepository.findAll())
                .firstVideoBitrateValueRange(0)
                .secondVideoBitrateValueRange(200000000)
                .firstAudioBitrateValueRange(0)
                .secondAudioBitrateValueRange(2048000)
                .audioChannels(audioChannelRepository.findAll())
                .firstVideoSizeRange(0.0)
                .secondVideoSizeRange(31457280.0)
                .audioCodecs(codecRepository.findByMediaType(MediaTypes.AUDIO))
                .videoCodecs(codecRepository.findByMediaType(MediaTypes.VIDEO))
                .genres(genreRepository.findAll())
                .audioLanguages(languageRepository.findAll())
                .build());
    }

    @Test
    void getConfiguration() {
        //Act
        configurationService.getConfiguration();
        Configuration configuration = configurationRepository.findById(1L).orElse(null);
        //Assert
        assertEquals(1L, configuration.getId());
        assertEquals(9000, configuration.getMaxDatesSaveFile());
        assertEquals(0, configuration.getMaxDatesControlFilesFromExternalSource());
        assertEquals(1, configuration.getVideoResolutions().size());
        assertEquals(0, configuration.getFirstVideoBitrateValueRange());
        assertEquals(200000000, configuration.getSecondVideoBitrateValueRange());
        assertEquals(0, configuration.getFirstAudioBitrateValueRange());
        assertEquals(2048000, configuration.getSecondAudioBitrateValueRange());
        assertEquals(2, configuration.getAudioChannels().size());
        assertEquals(0.0, configuration.getFirstVideoSizeRange());
        assertEquals(31457280.0, configuration.getSecondVideoSizeRange());
        assertEquals(2, configuration.getAudioCodecs().size());
        assertEquals(1, configuration.getVideoCodecs().size());
        assertEquals(3, configuration.getGenres().size());
        assertEquals(1, configuration.getAudioLanguages().size());

    }

    @Test
    void getGenresFromConfiguration() {
        //Arrest
        Configuration configuration = configurationRepository.findById(1L).orElse(null);
        //Act
        List<Genre> configurationGenres = configurationService.getGenresFromConfiguration(configuration);
        //Assert
        assertEquals(3, configurationGenres.size());
        assertEquals("Action", configurationGenres.stream()
                .filter(g-> g.getTitle().equals("Action")).findFirst()
                .map(Genre::getTitle)
                .orElse(null));
        assertEquals("Adventure", configurationGenres.stream()
                .filter(g-> g.getTitle().equals("Adventure"))
                .findFirst()
                .map(Genre::getTitle)
                .orElse(null));
        assertEquals("Animation", configurationGenres.stream()
                .filter(g-> g.getTitle().equals("Animation"))
                .findFirst()
                .map(Genre::getTitle)
                .orElse(null));
    }

    @Test
    void getVideoResolutionFromConfiguration() {
        //Arrest
        Configuration configuration = configurationRepository.findById(1L).orElse(null);
        //Act
        List<Resolution> configurationResolutions = configurationService.getVideoResolutionFromConfiguration(configuration);
        //Assert
        assertEquals(configuration.getVideoResolutions().size(), configurationResolutions.size());
        assertEquals("720p", configurationResolutions.stream()
                .filter(r -> r.getName().equals("720p"))
                .findFirst()
                .map(Resolution::getName)
                .orElse(null)
        );
    }

    @Test
    void getAudioCodecsFromConfiguration() {
    }

    @Test
    void getAudioChannelsFromConfiguration() {
    }

    @Test
    void getLanguagesFromConfiguration() {
    }

    @Test
    void updateConfiguration() {
        //Arrest
        Configuration configuration = configurationRepository.findById(1L).orElse(null);
        Genre action = genreRepository.findGenreByTitle("Action").orElse(null);
        List<Long> genres = List.of(action.getId());
        ConfigurationDto configurationDto = ConfigurationDto.builder()
                .genreIds(genres)
                .build();
        //Act
        configurationService.updateConfiguration(configurationDto);
        configuration = configurationRepository.findById(1L).orElse(null);
        //Assert
        assertNotNull(configuration);
        assertNotNull(configuration.getGenres());
        assertEquals(1, configuration.getGenres().size());
        assertEquals("Action", configuration.getGenres().stream()
                .filter(g -> g.getTitle().equals("Action"))
                .findFirst()
                .map(Genre::getTitle)
                .orElse(null));
        assertEquals(1L, configuration.getId());
        assertEquals(9000, configuration.getMaxDatesSaveFile());
        assertEquals(0, configuration.getMaxDatesControlFilesFromExternalSource());
        assertEquals(1, configuration.getVideoResolutions().size());
        assertEquals(0, configuration.getFirstVideoBitrateValueRange());
        assertEquals(200000000, configuration.getSecondVideoBitrateValueRange());
        assertEquals(0, configuration.getFirstAudioBitrateValueRange());
        assertEquals(2048000, configuration.getSecondAudioBitrateValueRange());
        assertEquals(2, configuration.getAudioChannels().size());
        assertEquals(0.0, configuration.getFirstVideoSizeRange());
        assertEquals(31457280.0, configuration.getSecondVideoSizeRange());
        assertEquals(2, configuration.getAudioCodecs().size());
        assertEquals(1, configuration.getVideoCodecs().size());
        assertEquals(1, configuration.getAudioLanguages().size());
    }
}