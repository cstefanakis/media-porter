package org.sda.mediaporter.controllers;

import jakarta.validation.Valid;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.dtos.LanguageDto;
import org.sda.mediaporter.models.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<Language>> getAllLanguages(){
        List<Language> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Language> getLanguageById(@PathVariable("id") Long id){
        Language language = languageService.getLanguageById(id);
        return ResponseEntity.ok(language);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-code")
    public ResponseEntity<Language> getLanguageByCode(@RequestParam("code") String code){
        Language language = languageService.getLanguageByCode(code);
        return ResponseEntity.ok(language);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-title")
    public ResponseEntity<Language> getLanguageByTitle(@RequestParam("title") String title){
        Language language =  languageService.getLanguageByTitle(title);
        return ResponseEntity.ok(language);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Language> createLanguage(@RequestBody @Valid LanguageDto languageDto){
        Language language = languageService.createLanguage(languageDto);
        return ResponseEntity.ok(language);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<Void> updateLanguageById(@PathVariable("id") Long id,
                                                   @RequestBody LanguageDto languageDto){
        languageService.updateLanguageById(id, languageDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguageById(@PathVariable Long id){
        languageService.deleteLanguageById(id);
        return ResponseEntity.noContent().build();
    }
}
