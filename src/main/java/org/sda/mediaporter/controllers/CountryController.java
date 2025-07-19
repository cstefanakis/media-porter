package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Services.CountryService;
import org.sda.mediaporter.dtos.CountryDto;
import org.sda.mediaporter.models.Country;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/by-code")
    public ResponseEntity<Country> getCountryByCode(@RequestParam("code") String code){
        Country country = countryService.getCountryByCode(code);
        return ResponseEntity.ok(country);
    }

    @GetMapping("/by-name")
    public ResponseEntity<Country> getCountryByName(@RequestParam("name") String name){
        Country country = countryService.getCountryByName(name);
        return ResponseEntity.ok(country);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable("id") Long id){
        Country country = countryService.getCountryById(id);
        return ResponseEntity.ok(country);
    }

    @GetMapping()
    public ResponseEntity<List <Country>> getAllCountries(){
        List<Country> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @PostMapping()
    public ResponseEntity<Country> createCountry(@RequestBody CountryDto countryDto){
        Country country = countryService.createCountry(countryDto);
        return ResponseEntity.ok(country);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCountry(@PathVariable("id") Long id,
                                                 @RequestBody CountryDto countryDto){
        countryService.updateCountry(id, countryDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountryById(@PathVariable("id") Long id){
        countryService.deleteCountryById(id);
        return ResponseEntity.ok().build();
    }
}
