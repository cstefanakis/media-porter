package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.Language;

public interface LanguageService {
    Language autoCreateLanguageByTitle(String title);
    Language autoCreateLanguageByCode(String code);
    Language getLanguageByCode(String code);
}
