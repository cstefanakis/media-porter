package org.sda.mediaporter.services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.CountryDto;
import org.sda.mediaporter.models.Country;

import java.util.List;

public interface CountryService {
    Country getCountryByCode(String countryCode);
    Country getCountryByName(String countryName);
    Country getCountryById(Long countryId);
    List<Country> getAllCountries();
    List<Country> getCountriesByCodes(List<String> countriesCodes);
    Country createCountry(@Valid CountryDto countryDto);
    Country autoCreateCountry(String country);
    Country getCountryByCodeOrNull(String countryCode);
    void updateCountry(Long countryId, CountryDto countryDto);
    void deleteCountryById(Long countryId);
}
