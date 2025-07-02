package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.enums.LanguageCodes;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    @Autowired
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

    private Language findLanguageByTitle(String englishTitle) {
        for(LanguageCodes languageCode : LanguageCodes.values()) {
            if(englishTitle.trim().equalsIgnoreCase(languageCode.getEnglishTitle())){
                return Language.builder()
                        .englishTitle(languageCode.getEnglishTitle())
                        .originalTitle(languageCode.getOriginalTitle())
                        .iso6391(languageCode.getIso6391())
                        .iso6392B(languageCode.getIso6392B())
                        .iso6392T(languageCode.getIso6392T())
                        .build();
            }
        }return null;
    }

    @Override
    public Language getLanguageByCode(String code) {
        return languageRepository.findByCode(code).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Language with code %s not found", code))
        );
    }

    @Override
    public Language getLanguageByEnglishTitle(String englishTitle) {
        return languageRepository.findByTitle(englishTitle).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Language with english title %s not found",englishTitle)));
    }

    @Override
    public Language autoCreateLanguageByCode(String code) {
        Optional<Language> optional = languageRepository.findByCode(code.trim().toLowerCase());
        if (optional.isPresent()) {
            return optional.get();
        }

        Language language = getLanguageByCode(code);
        if (language != null) {
            return languageRepository.save(language);
        }

       return null;
    }
}
