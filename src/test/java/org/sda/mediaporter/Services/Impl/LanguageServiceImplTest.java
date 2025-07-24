package org.sda.mediaporter.Services.Impl;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

//Integration Testing
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LanguageServiceImplTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageService languageService;

    @BeforeEach
    void setup() {
        languageRepository.save(Language.builder()
                .iso6391("cs")
                .iso6392B("cze")
                .iso6392T("ces")
                .englishTitle("Czech Republic")
                .originalTitle("Čeština")
                .build());
    }

    @Test
    void autoCreateLanguageByTitle() {
    }

    @Test
    void getAllLanguages() {

    }

    @Test
    void getLanguageByCode() {

    }

    @Test
    void getLanguageByTitle() {
    }

    @Test
    void getLanguageById() {
        // Arrange
        Language saved = languageRepository.save(Language.builder()
                .englishTitle("Czech")
                .iso6391("cs")
                .iso6392B("cze")
                .iso6392T("ces")
                .originalTitle("Čeština")
                .build());

        // Act
        Language found = languageService.getLanguageById(saved.getId());

        // Assert
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getEnglishTitle(), found.getEnglishTitle());
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