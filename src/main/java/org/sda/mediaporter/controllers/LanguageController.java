package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Servicies.LanguageService;
import org.sda.mediaporter.dtos.LanguageRequestDto;
import org.sda.mediaporter.models.multimedia.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @RequestMapping()
    public ResponseEntity<List<Language>> getLanguages() {
        List<Language> languages = languageService.getLanguages();
        return ResponseEntity.status(HttpStatus.OK).body(languages);
    }

    @RequestMapping("/by-name/{languageName}")
    public ResponseEntity<Language> getLanguageByName(
            @PathVariable("languageName") String languageName) {
        Language language = languageService.getLanguageByName(languageName);
        return ResponseEntity.status(HttpStatus.OK).body(language);
    }

    @RequestMapping("/by-code/{languageCode}")
    public ResponseEntity<Language> getLanguageByCode(
            @PathVariable String languageCode) {
        Language language = languageService.getLanguageByCode(languageCode);
        return ResponseEntity.status(HttpStatus.OK).body(language);
    }

    @RequestMapping("/{languageId}")
    public ResponseEntity<Language> getLanguageById(
            @PathVariable Long languageId){
        Language language = languageService.getLanguageById(languageId);
        return ResponseEntity.status(HttpStatus.OK).body(language);
    }

    @PostMapping("/create")
    public ResponseEntity<Language> createLanguage(@RequestBody LanguageRequestDto languageRequestDto) {
        Language language = languageService.createLanguage(languageRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(language);
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<Void> deleteLanguage(
            @PathVariable Long languageId) {
        languageService.deleteLanguage(languageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<Void> updateLanguage(
            @PathVariable long languageId,
            @RequestBody LanguageRequestDto languageRequestDto) {
        languageService.updateLanguageById(languageId, languageRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
