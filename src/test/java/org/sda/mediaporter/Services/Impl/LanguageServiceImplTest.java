package org.sda.mediaporter.Services.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Integration Testing
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LanguageServiceImplTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private SubtitleRepository subtitleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private LanguageService languageService;

    private Language czechLanguage;
    private Language englishLanguage;

    @BeforeEach
    void setup() {
        configurationRepository.deleteAll();
        audioRepository.deleteAll();
        subtitleRepository.deleteAll();
        movieRepository.deleteAll();
        languageRepository.deleteAll();
        czechLanguage = languageRepository.save(Language.builder()
                .iso6391("cs")
                .iso6392B("cze")
                .iso6392T("ces")
                .englishTitle("Czech")
                .originalTitle("Čeština")
                .build());
        englishLanguage = languageRepository.save(Language.builder()
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .englishTitle("English")
                .originalTitle("English")
                .build());
    }

    @Test
    void autoCreateLanguageByTitle() {
    }

    @Test
    void getAllLanguages() {
        //Act
        List<Language> allLanguages = languageService.getAllLanguages();
        //Assert
        assertEquals(2, allLanguages.size());
    }

    @Test
    void getLanguageByCode() {
        //Act
        Language englishLanguageWithCodeEn = languageService.getLanguageByCode("en");
        Language englishLanguageWithCodeEng = languageService.getLanguageByCode("eng");
        Language czechLanguageWithCodeCs = languageService.getLanguageByCode("cs");
        Language czechLanguageWithCodeCze = languageService.getLanguageByCode("CzE");
        Language czechLanguageWithCodeCes = languageService.getLanguageByCode("cEs");

        //Arrest
        assertEquals("English", englishLanguageWithCodeEn.getEnglishTitle());
        assertEquals("English", englishLanguageWithCodeEng.getEnglishTitle());
        assertEquals("Czech", czechLanguageWithCodeCs.getEnglishTitle());
        assertEquals("Czech", czechLanguageWithCodeCze.getEnglishTitle());
        assertEquals("Czech", czechLanguageWithCodeCes.getEnglishTitle());
    }

    @Test
    void getLanguageByTitle() {
    }

    @Test
    void getLanguageById() {
        // Act
        Language found = languageService.getLanguageById(czechLanguage.getId());

        // Assert
        assertNotNull(found);
        assertEquals(czechLanguage.getId(), found.getId());
        assertEquals(czechLanguage.getEnglishTitle(), found.getEnglishTitle());
    }

    @Test
    void createLanguage() {
    }

    @Test
    void updateLanguageById() {
    }

    @Test
    void deleteLanguageById() {
    }
}