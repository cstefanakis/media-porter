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

        language.setEnglishTitle(languageDto.getEnglishTitle() == null? language.getEnglishTitle(): validatedTitle(languageDto.getEnglishTitle()));
        language.setOriginalTitle(languageDto.getOriginalTitle() == null? language.getOriginalTitle(): validatedTitle(languageDto.getOriginalTitle()));
        language.setIso6391(languageDto.getIso6391() == null? language.getIso6391(): validatedCode(languageDto.getIso6391()));
        language.setIso6392T(languageDto.getIso6392T() == null? language.getIso6392T(): validatedCode(languageDto.getIso6392T()));
        language.setIso6392B(languageDto.getIso6392B() == null? language.getIso6392B(): validatedCode(languageDto.getIso6392B()));
        return language;
    }

    private String validatedCode(String codeIso){
        codeIso = codeIso.toLowerCase().trim();
        Optional<Language> languageWithCodeIso = languageRepository.findByCode(codeIso);
        if(languageWithCodeIso.isPresent()){
            Language language = languageWithCodeIso.get();
            String iso6391 = language.getIso6391().toLowerCase().trim();
            String iso6329B = language.getIso6392B().toLowerCase().trim();
            String iso6329T = language.getIso6392T().toLowerCase().trim();
            if(iso6391.equals(codeIso) || iso6329B.equals(codeIso) || iso6329T.equals(codeIso)){
                return codeIso;
            }
        }
        if(languageWithCodeIso.isEmpty()){
            return codeIso;
        }
        throw new EntityExistsException(String.format("Language with code %s is exist",codeIso));
    }

    private String validatedTitle(String title){
        title = title.toLowerCase().trim();
        Optional<Language> languageTitle = languageRepository.findByTitle(title);
        if(languageTitle.isPresent()){
            Language language = languageTitle.get();
            String originalTitle = language.getOriginalTitle().toLowerCase().trim();
            String englishTitle = language.getEnglishTitle().toLowerCase().trim();
            if(englishTitle.equals(title) || originalTitle.equals(title)){
                return title;
            }
        }
        if(languageTitle.isEmpty()){
            return title;
        }
        throw new EntityExistsException(String.format("Language with title %s is exist",title));
    }
}
