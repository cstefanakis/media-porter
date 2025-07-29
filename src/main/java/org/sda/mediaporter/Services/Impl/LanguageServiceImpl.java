package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.dtos.LanguageDto;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Language autoCreateLanguageByTitle(String title) {
        Optional<Language> languageOptional = languageRepository.findByTitle(title.trim().toLowerCase());
        if (languageOptional.isPresent()) {
            return languageOptional.get();
        }

        if (!title.isEmpty()) {
            return languageRepository.save(Language.builder().englishTitle(title).build());
        }
        return null;
    }

    @Override
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    private Language findLanguageByTitle(String languageTitle) {
        return languageRepository.findByTitle(languageTitle).orElseThrow(()-> new EntityNotFoundException(String.format("Language with title %s not found", languageTitle)));
    }

    @Override
    public Language getLanguageByCode(String code) {
        return languageRepository.findByCode(code).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Language with code %s not found", code))
        );
    }

    @Override
    public Language getLanguageByTitle(String title) {
        return languageRepository.findByTitle(title).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Language with english title %s not found",title)));
    }

    @Override
    public Language getLanguageById(Long id) {
        return languageRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Language with id %s not found",id)));
    }

    @Override
    public Language createLanguage(LanguageDto languageDto) {
        return languageRepository.save(toEntity(new Language(), languageDto));
    }

    @Override
    public void updateLanguageById(Long id, LanguageDto languageDto) {
        Language language = getLanguageById(id);
        languageRepository.save(toEntity(language, languageDto));
    }

    @Override
    public void deleteLanguageById(Long id) {
        Language language = getLanguageById(id);
        languageRepository.delete(language);
    }

    private Language toEntity(Language language, LanguageDto languageDto){

        language.setEnglishTitle(validatedTitle(language.getEnglishTitle(), languageDto.getEnglishTitle()));
        language.setOriginalTitle(validatedTitle(language.getOriginalTitle(), languageDto.getOriginalTitle()));
        language.setIso6391(validatedCode(language.getIso6391(), languageDto.getIso6391()));
        language.setIso6392T(validatedCode(language.getIso6392T(), languageDto.getIso6392T()));
        language.setIso6392B(validatedCode(language.getIso6392B(), languageDto.getIso6392B()));
        return language;
    }

    private String validatedCode(String codeIso, String codeIsoDto){

        if(codeIsoDto == null) {
            return codeIso;
        }

        Optional<Language> languageWithCodeIso = languageRepository.findByCode(codeIsoDto);

        if(languageWithCodeIso.isPresent()) {
            if (codeIso != null && codeIso.equalsIgnoreCase(codeIsoDto)) {
                return codeIsoDto.trim();
            }
        }

        if(languageWithCodeIso.isEmpty()){
            return codeIsoDto.trim();
        }

        throw new EntityExistsException(String.format("Language with code %s is exist",codeIso));
    }

    private String validatedTitle(String title, String titleDto){

        if(titleDto == null) {
            return title;
        }

        Optional<Language> languageWithTitle = languageRepository.findByTitle(titleDto);

        if(languageWithTitle.isPresent()) {
            if (title != null && title.equalsIgnoreCase(titleDto)) {
                return title;
            }
        }

        if(languageWithTitle.isEmpty()){
            return titleDto;
        }

        throw new EntityExistsException(String.format("Language with title %s is exist",titleDto));
    }
}
