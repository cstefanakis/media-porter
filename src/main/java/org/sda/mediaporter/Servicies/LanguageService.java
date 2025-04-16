package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.dtos.LanguageRequestDto;
import org.sda.mediaporter.models.Language;

public interface LanguageService {
    Language getLanguageByName(String language);
    Language getLanguageById(Long languageId);
    Language getLanguageByCode(String languageCode);
    Language createLanguage(LanguageRequestDto languageRequestDto);
    void updateLanguageByName(String name, LanguageRequestDto languageRequestDto);
    void deleteLanguage(Language language);
}
