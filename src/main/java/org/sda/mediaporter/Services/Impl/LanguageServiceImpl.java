package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.dtos.LanguageDto;
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

        Optional<Language> languageWithCodeIso = languageRepository.findByCode(codeIsoDto);

        if(codeIsoDto == null) {
            return codeIso;
        }

        codeIsoDto = codeIsoDto.trim();

        if(languageWithCodeIso.isPresent()) {
            Language language = languageWithCodeIso.get();
            if (language.getIso6391().equals(codeIso) || language.getIso6392B().equals(codeIso) || language.getIso6392T().equals(codeIso)) {
                return codeIsoDto;
            }
        }

        if(languageWithCodeIso.isEmpty()){
            return codeIsoDto;
        }

        throw new EntityExistsException(String.format("Language with code %s is exist",codeIso));
    }

    private String validatedTitle(String title, String titleDto){

        Optional<Language> languageWithTitle = languageRepository.findByTitle(titleDto);

        if(titleDto == null) {
            return title;
        }

        titleDto = titleDto.trim();

        if(languageWithTitle.isPresent()) {
            Language language = languageWithTitle.get();
            if (language.getOriginalTitle().equals(title) || language.getEnglishTitle().equals(title)) {
                return titleDto;
            }
        }

        if(languageWithTitle.isEmpty()){
            return titleDto;
        }

        throw new EntityExistsException(String.format("Language with title %s is exist",titleDto));
    }
}
