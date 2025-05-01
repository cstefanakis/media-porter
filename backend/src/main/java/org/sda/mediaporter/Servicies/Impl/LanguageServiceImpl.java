package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.LanguageService;
import org.sda.mediaporter.api.LanguageApi;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Language autoCreateLanguageByTitle(String title) {
        Optional<Language> optional = languageRepository.findByTitle(title.trim().toLowerCase());
        if (optional.isPresent()) {
            return optional.get();
        }

        Language language = findLanguageByTitle(title);
        if (language != null) {
            return languageRepository.save(language);
        }
        return null;
    }

    private Language findLanguageByTitle(String title) {
        List<Language> languages = new LanguageApi().getLanguages();
        return languages.stream().filter(l -> l.getEnglishTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
    }

    private Language findLanguageByCode(String code) {
        List<Language> languages = new LanguageApi().getLanguages();
        return languages.stream().filter(l -> l.getCode().trim().equals(code.trim())).toList().getFirst();
    }

    @Override
    public Language autoCreateLanguageByCode(String code) {
        Optional<Language> optional = languageRepository.findByCode(code.trim().toLowerCase());
        if (optional.isPresent()) {
            return optional.get();
        }

        Language language = findLanguageByCode(code);
        if (language != null) {
            return languageRepository.save(language);
        }

       return null;
    }
}
