package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Language;

public interface LanguageService {
    Language autoCreateLanguageByTitle(String title);
    Language autoCreateLanguageByCode(String code);
}
