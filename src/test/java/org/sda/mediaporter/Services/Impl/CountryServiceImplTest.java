package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sda.mediaporter.Services.CountryService;
import org.sda.mediaporter.dtos.CountryDto;
import org.sda.mediaporter.models.Country;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.CountryRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CountryServiceImplTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private CountryService countryService;

    private Country unitedKingdom;
    private Country greece;

    @BeforeEach
    void setup(){
        configurationRepository.deleteAll();
        movieRepository.deleteAll();
        countryRepository.deleteAll();

        unitedKingdom = countryRepository.save(
                Country.builder()
                        .iso2Code("GB")
                        .iso3Code("GBR")
                        .englishName("United Kingdom")
                        .nativeName("United Kingdom")
                        .build()
        );

        greece = countryRepository.save(
                Country.builder()
                        .iso2Code("GR")
                        .iso3Code("GRC")
                        .englishName("Greece")
                        .nativeName("Greece").build()
        );
    }

    @Test
    void getCountryByCode_successfully() {
        //Arrest
        String code = "gB";

        //Act
        Country country = countryService.getCountryByCode(code);

        //Assert
        assertEquals("United Kingdom", country.getEnglishName());

    }

    @Test
    void getCountryByCode_noCodeFind() {
        //Arrest
        String code = "aaa";

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> countryService.getCountryByCode(code));

    }

    @Test
    void getCountryByName_successfully() {
        //Arrest
        String name = "United Kingdom";

        //Act
        Country country = countryService.getCountryByName(name);

        //Assert
        assertEquals("United Kingdom", country.getEnglishName());
    }

    @Test
    void getCountryByName_noCodeFind() {
        //Arrest
        String name = "no name";

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> countryService.getCountryByName(name));

    }

    @Test
    void getCountryById_successfully() {
        //Arrest
        Long id = unitedKingdom.getId();

        //Act
        Country country = countryService.getCountryById(id);

        //Assert
        assertEquals("GB", country.getIso2Code());
    }

    @Test
    void getAllCountries() {
        //Act
        List<Country> countries = countryService.getAllCountries();

        //Assert
        assertEquals(2, countries.size());
    }

    @Test
    void createCountry_withNullIso2Code() {
        //Arrest
        CountryDto columbiaCountryDto = CountryDto.builder()
                .iso3Code("COL")
                .englishName("Colombia")
                .nativeName("Colombia").build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> countryService.createCountry(columbiaCountryDto));
    }

    @Test
    void createCountry_withNullIso3Code() {
        //Arrest
        CountryDto columbiaCountryDto = CountryDto.builder()
                .iso2Code("CO")
                .englishName("Colombia")
                .nativeName("Colombia").build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> countryService.createCountry(columbiaCountryDto));
    }

    @Test
    void createCountry_withNullEnglishName() {
        //Arrest
        CountryDto columbiaCountryDto = CountryDto.builder()
                .iso2Code("CO")
                .iso3Code("COL")
                .nativeName("Colombia")
                .build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> countryService.createCountry(columbiaCountryDto));
    }

    @Test
    void createCountry_withNullNativeName() {
        //Arrest
        CountryDto columbiaCountryDto = CountryDto.builder()
                .iso2Code("CO")
                .iso3Code("COL")
                .englishName("Colombia")
                .build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> countryService.createCountry(columbiaCountryDto));
    }

    @Test
    void createCountry_withExistIso2Code() {
        //Arrest
        CountryDto columbiaCountryDto = CountryDto.builder()
                .iso2Code("GB")
                .iso3Code("COL")
                .englishName("Colombia")
                .nativeName("Colombia").build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> countryService.createCountry(columbiaCountryDto));
    }

    @Test
    void createCountry_withExistIso3Code() {
        //Arrest
        CountryDto columbiaCountryDto = CountryDto.builder()
                .iso2Code("CO")
                .iso3Code("GBR")
                .englishName("Colombia")
                .nativeName("Colombia").build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> countryService.createCountry(columbiaCountryDto));
    }

    @Test
    void createCountry_withExistEnglishName() {
        //Arrest
        CountryDto columbiaCountryDto = CountryDto.builder()
                .iso2Code("CO")
                .iso3Code("COL")
                .englishName("United Kingdom")
                .nativeName("Colombia").build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> countryService.createCountry(columbiaCountryDto));
    }

    @Test
    void createCountry_withExistNativeName() {
        //Arrest
        CountryDto columbiaCountryDto = CountryDto.builder()
                .iso2Code("CO")
                .iso3Code("COL")
                .englishName("Colombia")
                .nativeName("United Kingdom").build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> countryService.createCountry(columbiaCountryDto));
    }

    @Test
    void createCountry_successfully() {
        //Arrest
        CountryDto columbiaCountryDto = CountryDto.builder()
                .iso2Code("CO")
                .iso3Code("COL")
                .englishName("Colombia")
                .nativeName("Colombia").build();

        //Act
        Country country = countryService.createCountry(columbiaCountryDto);

        //Assert
        assertNotNull(country.getId());
    }

    @Test
    void updateCountry_withExistIso2Code() {
        //Arrest
        Long greeceCountryId = greece.getId();
        CountryDto greeceUpdateCountryDto = CountryDto.builder()
                .iso2Code("GB")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> countryService.updateCountry(greeceCountryId, greeceUpdateCountryDto));
    }

    @Test
    void updateCountry_withExistIso3Code() {
        //Arrest
        Long greeceCountryId = greece.getId();
        CountryDto greeceUpdateCountryDto = CountryDto.builder()
                .iso3Code("GBR")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> countryService.updateCountry(greeceCountryId, greeceUpdateCountryDto));
    }

    @Test
    void updateCountry_withExistEnglishName() {
        //Arrest
        Long greeceCountryId = greece.getId();
        CountryDto greeceUpdateCountryDto = CountryDto.builder()
                .englishName("United Kingdom")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> countryService.updateCountry(greeceCountryId, greeceUpdateCountryDto));
    }

    @Test
    void updateCountry_withExistNativeName() {
        //Arrest
        Long greeceCountryId = greece.getId();
        CountryDto greeceUpdateCountryDto = CountryDto.builder()
                .nativeName("United Kingdom")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> countryService.updateCountry(greeceCountryId, greeceUpdateCountryDto));
    }

    @Test
    void updateCountry_withAllNullParameters() {
        //Arrest
        Long greeceCountryId = greece.getId();
        CountryDto greeceUpdateCountryDto = CountryDto.builder()
                .build();

        //Act
        countryService.updateCountry(greeceCountryId, greeceUpdateCountryDto);
        Country country = countryService.getCountryById(greeceCountryId);

        //Assert
        assertEquals("GR", country.getIso2Code());
        assertEquals("GRC", country.getIso3Code());
        assertEquals("Greece", country.getEnglishName());
        assertEquals("Greece", country.getNativeName());
    }

    @Test
    void updateCountry_successfully() {
        //Arrest
        Long greeceCountryId = greece.getId();
        CountryDto greeceUpdateCountryDto = CountryDto.builder()
                .iso2Code("AF")
                .iso3Code("AFG")
                .englishName("Afghanistan")
                .nativeName("Afghanistan")
                .build();

        //Act
        countryService.updateCountry(greeceCountryId, greeceUpdateCountryDto);
        Country country = countryService.getCountryById(greeceCountryId);

        //Assert
        assertEquals("AF", country.getIso2Code());
        assertEquals("AFG", country.getIso3Code());
        assertEquals("Afghanistan", country.getEnglishName());
        assertEquals("Afghanistan", country.getNativeName());
    }

    @Test
    void deleteCountryById_notExistId() {
        //Arrest
        Long notExistId = 0L;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> countryService.deleteCountryById(notExistId));
    }

    @Test
    void deleteCountryById_successfully() {
        //Arrest
        Long greeceId = greece.getId();

        //Act
        countryService.deleteCountryById(greeceId);

        //Assert
        assertThrows(EntityNotFoundException.class, ()-> countryService.getCountryById(greeceId));
    }
}