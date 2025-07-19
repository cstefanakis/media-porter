package org.sda.mediaporter.Services;

import org.sda.mediaporter.dtos.CountryDto;
import org.sda.mediaporter.models.Country;

import java.util.List;

public interface CountryService {
    Country getCountryByCode(String countryCode);
    Country getCountryByName(String countryName);
    Country getCountryById(Long countryId);
    List<Country> getAllCountries();
    Country createCountry(CountryDto countryDto);
    void updateCountry(Long countryId, CountryDto countryDto);
    void deleteCountryById(Long countryId);
}
