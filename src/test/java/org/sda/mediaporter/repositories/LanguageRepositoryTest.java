package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository languageRepository;

    @BeforeEach
    void loadData(){
        languageRepository.save(Language.builder()
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .englishTitle("English")
                .originalTitle("English")
                .build());
        languageRepository.save(Language.builder()
                .iso6391("es")
                .iso6392B("spa")
                .iso6392T("spa")
                .englishTitle("Spanish")
                .originalTitle("Español")
                .build());
    }

    @Test
    void findByTitle_true() {
        //Arrest
        String title = "English";
        //Act
        Optional<Language> result = languageRepository.findByTitle(title);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findByTitle_trueWithOriginalLanguage() {
        //Arrest
        String title = "Español";
        //Act
        Optional<Language> result = languageRepository.findByTitle(title);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findByTitle_false() {
        //Arrest
        String title = "Germany";
        //Act
        Optional<Language> result = languageRepository.findByTitle(title);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByCode_trueWithCode3(){
        //Arrest
        String code = "spa";
        //Act
        Optional<Language> result = languageRepository.findByCode(code);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findByCode_trueWithCode2(){
        //Arrest
        String code = "es";
        //Act
        Optional<Language> result = languageRepository.findByCode(code);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findByCode_trueWithCode2_false(){
        //Arrest
        String code = "el";
        //Act
        Optional<Language> result = languageRepository.findByCode(code);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByCodeOrTitle() {
        //Arrest
        String code2 = "en";
        String code3 ="eng";
        String originalTitle = "Español";
        String title = "English";
        //Act
        Optional<Language> code2Result = languageRepository.findByCodeOrTitle(code2);
        Optional<Language> code3Result = languageRepository.findByCodeOrTitle(code3);
        Optional<Language> originalTitleResult = languageRepository.findByCodeOrTitle(originalTitle);
        Optional<Language> titleResult = languageRepository.findByCodeOrTitle(title);
        //Assert
        assertTrue(code2Result.isPresent());
        assertTrue(code3Result.isPresent());
        assertTrue(originalTitleResult.isPresent());
        assertTrue(titleResult.isPresent());
    }
}