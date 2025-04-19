package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.dtos.LanguageRequestDto;
import org.sda.mediaporter.models.multimedia.Language;

import java.util.List;

public interface LanguageService {
    List<Language> getLanguages();
    Language getLanguageByName(String language);
    Language getLanguageById(Long languageId);
    Language getLanguageByCode(String languageCode);
    Language createLanguage(LanguageRequestDto languageRequestDto);
    void updateLanguageById(Long id, LanguageRequestDto languageRequestDto);
    void deleteLanguage(Long languageId);
}
