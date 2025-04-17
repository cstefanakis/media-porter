package org.sda.mediaporter.Servicies.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Servicies.LanguageService;
import org.sda.mediaporter.dtos.LanguageRequestDto;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }


    @Override
    public List<Language> getLanguages() {
        return languageRepository.findAll();
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
    public void updateLanguageById(Long languageId, LanguageRequestDto languageRequestDto) {
        Language language = getLanguageById(languageId);
        languageRepository.save(toEntity(language, languageRequestDto));
    }

    @Override
    public void deleteLanguage(Long languageId) {
        Language language = getLanguageById(languageId);
        languageRepository.delete(language);
    }
    private Language toEntity(Language language, LanguageRequestDto languageRequestDto) {
        language.setName(validatedName(language.getName(), languageRequestDto.getName()));
        language.setCode(validatedCode(language.getCode(), languageRequestDto.getCode()));
        return language;
    }

    //Validate if language name exist
    private String validatedName(String currentName, String name) {
        Optional<Language> languageOptional = languageRepository.findByName(name);
        if (languageOptional.isPresent() && !currentName.equals(languageOptional.get().getName())) {
            throw new EntityExistsException(String.format("Language with name %s already exists", name));
        }
        return name;
    }

    //Validate if language code exist
    private String validatedCode(String currentCode, String code) {
        Optional<Language> languageCode = languageRepository.findByCode(code);
        if (languageCode.isPresent() && !currentCode.equals(languageCode.get().getName())) {
            throw new EntityExistsException(String.format("Language with code %s already exists", code));
        }
        return code;
    }

}
