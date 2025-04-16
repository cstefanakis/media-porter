package org.sda.mediaporter.Servicies.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Servicies.LanguageService;
import org.sda.mediaporter.dtos.LanguageRequestDto;
import org.sda.mediaporter.models.Language;
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
    public Language getLanguageByName(String name) {
        return languageRepository.findByName(name)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Language with name %s not found", name)));
    }

    @Override
    public Language getLanguageById(Long languageId) {
        return languageRepository.findById(languageId)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Language with id %s not found", languageId)));
    }

    @Override
    public Language getLanguageByCode(String languageCode) {
        return languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Language with code %s not found", languageCode)));
    }

    @Override
    public Language createLanguage(LanguageRequestDto languageRequestDto) {
        return languageRepository.save(toEntity(new Language(), languageRequestDto));
    }

    @Override
    public void updateLanguageByName(String name, LanguageRequestDto languageRequestDto) {
        Language language = getLanguageByName(name);
        languageRepository.save(toEntity(language, languageRequestDto));
    }

    @Override
    public void deleteLanguage(Language language) {

    }
    private Language toEntity(Language language, LanguageRequestDto languageRequestDto) {
        language.setName(languageRequestDto.getName());
        language.setCode(languageRequestDto.getCode());
        return language;
    }

    private String validatedName(String name) {
        Optional<Language> languageOptional = languageRepository.findByName(name);
        if (languageOptional.isPresent()) {
            throw new EntityExistsException(String.format("Language with name %s already exists", name));
        }
        return name;
    }

}
