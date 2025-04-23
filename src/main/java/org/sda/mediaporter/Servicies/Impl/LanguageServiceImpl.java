package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.LanguageService;
import org.sda.mediaporter.api.LanguageApi;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Language autoCreateLanguageByTitle(String title) {
        Optional <Language> optional = languageRepository.findByTitle(title);
        if(optional.isPresent()) {
            return optional.get();
        }
        return languageRepository.save(Objects.requireNonNull(findLanguageByTitle(title)));
    }

    private Language findLanguageByTitle(String title) {
        title.replace(" ", "");
        List<Language> languages = new LanguageApi().getLanguages();
        for(Language language : languages) {
            if(language.getEnglishTitle().equalsIgnoreCase(title)) {
                return language;
            }
        }return null;
    }

    private Language findLanguageByCode(String code) {
        List<Language> languages = new LanguageApi().getLanguages();
        return languages.stream().filter(l -> l.getCode().equals(code)).toList().getFirst();
    }

    @Override
    public Language autoCreateLanguageByCode(String code) {
       Optional<Language> optional = languageRepository.findByCode(code);
       return optional.orElseGet(() -> languageRepository.save(findLanguageByCode(code)));
    }
}
