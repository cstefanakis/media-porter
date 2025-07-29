package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.dtos.LanguageDto;
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
    void autoCreateLanguageByTitle_getLanguageFromDb() {
        //Arrest
        String title = "english";

        //Act
        Language language = languageService.autoCreateLanguageByTitle(title);

        //Assert
        assertEquals("en", language.getIso6391());
    }

    @Test
    void autoCreateLanguageByTitle_createNewLanguage() {
        //Arrest
        String title = "Italian";

        //Act
        Language language = languageService.autoCreateLanguageByTitle(title);

        //Assert
        assertEquals("Italian", languageService.getLanguageByTitle("Italian").getEnglishTitle());
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
        Language czechLanguageWithCodeCze = languageService.getLanguageByCode("cze");
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
        //Act
        Language languageByEnglishTitle = languageService.getLanguageByTitle("English");
        Language languageByOriginalTitle = languageService.getLanguageByTitle("Čeština");
        //Arrest
        assertEquals("English", languageByEnglishTitle.getEnglishTitle());
        assertEquals("Czech", languageByOriginalTitle.getEnglishTitle());
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
        //Arrange
        LanguageDto languageDto = LanguageDto.builder()
                .iso6391("es")
                .iso6392B("spa")
                .iso6392T("spa")
                .englishTitle("Spanish")
                .originalTitle("Español")
                .build();
        //Act
        Language createdLanguage = languageService.createLanguage(languageDto);
        //Arrest
        assertEquals("Spanish", createdLanguage.getEnglishTitle());
    }

    @Test
    void createLanguage_WithEmptyEnglishTitle() {
        //Arrange
        LanguageDto languageDto = LanguageDto.builder()
                .iso6391("fr")
                .iso6392B("fre")
                .iso6392T("fra")
                .originalTitle("Français")
                .build();
        //Arrest and Act
        assertThrows(RuntimeException.class, () -> {
            languageService.createLanguage(languageDto);
        });
    }

    @Test
    void createLanguage_WithSameParameterIso6391() {
        //Arrange
        LanguageDto languageDto = LanguageDto.builder()
                .iso6391("en")
                .iso6392B("ger")
                .iso6392T("deu")
                .englishTitle("German")
                .originalTitle("Deutsch").build();

        //Arrest and Act
        assertThrows(EntityExistsException.class, ()-> languageService.createLanguage(languageDto));
    }

    @Test
    void createLanguage_WithSameParameterIso6392B() {
        //Arrange
        LanguageDto languageDto = LanguageDto.builder()
                .iso6391("de")
                .iso6392B("eng")
                .iso6392T("deu")
                .englishTitle("German")
                .originalTitle("Deutsch").build();

        //Arrest and Act
        assertThrows(EntityExistsException.class, ()-> languageService.createLanguage(languageDto));
    }

    @Test
    void createLanguage_WithSameParameterIso6392T() {
        //Arrange
        LanguageDto languageDto = LanguageDto.builder()
                .iso6391("de")
                .iso6392B("ger")
                .iso6392T("eng")
                .englishTitle("German")
                .originalTitle("Deutsch").build();

        //Arrest and Act
        assertThrows(EntityExistsException.class, ()-> languageService.createLanguage(languageDto));
    }

    @Test
    void createLanguage_WithSameParameterEnglishTitle() {
        //Arrange
        LanguageDto languageDto = LanguageDto.builder()
                .iso6391("de")
                .iso6392B("ger")
                .iso6392T("deu")
                .englishTitle("English")
                .originalTitle("Deutsch").build();

        //Arrest and Act
        assertThrows(EntityExistsException.class, ()-> languageService.createLanguage(languageDto));
    }

    @Test
    void createLanguage_WithSameParameterOriginalTitle() {
        //Arrange
        LanguageDto languageDto = LanguageDto.builder()
                .iso6391("de")
                .iso6392B("ger")
                .iso6392T("deu")
                .englishTitle("German")
                .originalTitle("English").build();

        //Arrest and Act
        assertThrows(EntityExistsException.class, ()-> languageService.createLanguage(languageDto));
    }

    @Test
    void updateLanguageById_updateWithAllNull() {
        //Arrange
        Long englishLanguageId =  englishLanguage.getId();
        LanguageDto languageDto = LanguageDto.builder()
                .build();


        //Act
        languageService.updateLanguageById(englishLanguageId, languageDto);

        //Assert
        assertEquals("English", languageService.getLanguageById(englishLanguageId).getEnglishTitle());
        assertEquals("en", languageService.getLanguageById(englishLanguageId).getIso6391());
        assertEquals("eng", languageService.getLanguageById(englishLanguageId).getIso6392B());
        assertEquals("eng", languageService.getLanguageById(englishLanguageId).getIso6392T());
        assertEquals("English", languageService.getLanguageById(englishLanguageId).getOriginalTitle());
    }

    @Test
    void updateLanguageById_updateEnglishTitle() {
        //Arrange
        Long englishLanguageId =  englishLanguage.getId();
        LanguageDto languageDto = LanguageDto.builder()
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .englishTitle("English_update")
                .originalTitle("English")
                .build();


        //Act
        languageService.updateLanguageById(englishLanguageId, languageDto);
        Language updatedLanguage = languageService.getLanguageById(englishLanguageId);

        //Assert
        assertEquals("English_update", updatedLanguage.getEnglishTitle());
    }

    @Test
    void deleteLanguageById() {
        //Arrange
        Long id = englishLanguage.getId();
        //Act
        languageService.deleteLanguageById(id);
        //Assert
        assertTrue(languageRepository.findById(id).isEmpty());
    }

    @Test
    void deleteLanguageById_notExist() {
        //Arrange
        Long id = 0L;
        //Act
        assertThrows(EntityNotFoundException.class, ()-> languageService.deleteLanguageById(id));
    }
}