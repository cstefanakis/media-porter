package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.CountryService;
import org.sda.mediaporter.dtos.CountryDto;
import org.sda.mediaporter.models.Country;
import org.sda.mediaporter.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country getCountryByCode(String countryCode) {
        return countryRepository.findCountryByCode(countryCode).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Country with code: %s not exist", countryCode))
        );
    }

    @Override
    public Country getCountryByName(String countryName) {
        return countryRepository.findCountryByName(countryName).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Country with name: %s not exist", countryName))
        );
    }

    @Override
    public Country getCountryById(Long countryId) {
        return countryRepository.findById(countryId).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Country with id: %s not exist", countryId))
        );
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country createCountry(CountryDto countryDto) {
        return countryRepository.save(toEntity(new Country(), countryDto));
    }

    @Override
    public void updateCountry(Long countryId, CountryDto countryDto) {
        Country country = getCountryById(countryId);
        countryRepository.save(toEntity(country, countryDto));
    }

    @Override
    public void deleteCountryById(Long countryId) {
        Country country = getCountryById(countryId);
        countryRepository.delete(country);
    }

    private Country toEntity(Country country, CountryDto countryDto){
        String countryIso2Code = country.getIso2Code();
        String countryIso2CodeDto = countryDto.getIso2Code();
        country.setIso2Code(validatedCode(countryIso2Code, countryIso2CodeDto));

        String countryIso3Code = country.getIso3Code();
        String countryIso3CodeDto = countryDto.getIso3Code();
        country.setIso3Code(validatedCode(countryIso3Code, countryIso3CodeDto));

        String countryEnglishName = country.getEnglishName();
        String countryEnglishNameDto = countryDto.getEnglishName();
        country.setEnglishName(validatedName(countryEnglishName, countryEnglishNameDto));

        String countryNativeName = country.getNativeName();
        String countryNativeNameDto = countryDto.getNativeName();
        country.setNativeName(validatedName(countryNativeName, countryNativeNameDto));
        return country;
    }

    private String validatedCode(String countryCode, String countryCodeDto){

        if(countryCodeDto == null){
            return countryCode;
        }
        if(countryCode != null && countryCode.equals(countryCodeDto)){
            return countryCodeDto;
        }
        Optional<Country> countryByCode = countryRepository.findCountryByCode(countryCodeDto);
        if(countryByCode.isEmpty()){
            return countryCodeDto;
        }
        throw new EntityExistsException(String.format("Country with code %s already exist", countryCodeDto));
    }

    private String validatedName(String countryName, String countryNameDto){
        if(countryNameDto == null){
            return countryName;
        }
        if(countryName!= null && countryName.equals(countryNameDto)){
            return countryNameDto;
        }
        Optional<Country> countryByCode = countryRepository.findCountryByName(countryNameDto);
        if(countryByCode.isEmpty()){
            return countryNameDto;
        }

        throw new EntityExistsException(String.format("Country with code %s already exist", countryNameDto));
    }
}

