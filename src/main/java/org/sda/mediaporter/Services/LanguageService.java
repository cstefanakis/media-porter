package org.sda.mediaporter.Services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.LanguageDto;
import org.sda.mediaporter.models.Language;

import java.util.List;

public interface LanguageService {
    Language autoCreateLanguageByTitle(String title);
    List<Language> getAllLanguages();
    Language getLanguageByCode(String code);
    Language getLanguageByTitle(String languageTitle);
    Language getLanguageById(Long id);
    Language createLanguage(@Valid LanguageDto languageDto);
    void updateLanguageById(Long id, LanguageDto languageDto);
    void deleteLanguageById(Long id);
}
