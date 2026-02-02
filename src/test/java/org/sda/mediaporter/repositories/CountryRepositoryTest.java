package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Country;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("Test")
@Import(TestDataFactory.class)
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Country afghanistan;
    private Country albania;

    @BeforeEach
    void loadData() {
        this.afghanistan = testDataFactory.createCountryAfghanistan();
        this.albania = testDataFactory.createCountryAlbania();
    }

    @Test
    void findCountryByCode_iso2Code() {
        //Arrest
        Long countryId = this.afghanistan.getId();
        String iso2code = "AF";
        //Act
        Optional<Country> result = countryRepository.findCountryByCode(iso2code);
        //Assert
        assertTrue(result.isPresent());
        assertEquals(countryId, result.get().getId());
    }

    @Test
    void findCountryByCode_iso3Code() {
        //Arrest
        Long countryId = this.afghanistan.getId();
        String iso3code = "AFG";
        //Act
        Optional<Country> result = countryRepository.findCountryByCode(iso3code);
        //Assert
        assertTrue(result.isPresent());
        assertEquals(countryId, result.get().getId());
    }

    @Test
    void findCountryByName() {
        //Arrest
        Long countryId = this.albania.getId();
        String title = "Albania";
        //Act
        Optional<Country> result = countryRepository.findCountryByName(title);
        //Assert
        assertTrue(result.isPresent());
        assertEquals(countryId, result.get().getId());
    }

    @Test
    void findCountryByNameOrCode() {
        //Arrest
        Long countryId = this.albania.getId();
        String title = "Albania";
        String code = "ALB";
        //Act
        Optional<Country> resultByTitle = countryRepository.findCountryByNameOrCode(title);
        Optional<Country> resultByCode = countryRepository.findCountryByNameOrCode(code);
        //Assert
        assertTrue(resultByTitle.isPresent());
        assertTrue(resultByCode.isPresent());
        assertEquals(countryId, resultByTitle.get().getId());
        assertEquals(countryId, resultByCode.get().getId());
    }
}