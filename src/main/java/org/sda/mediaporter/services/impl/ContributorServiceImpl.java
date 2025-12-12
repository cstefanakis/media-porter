package org.sda.mediaporter.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCrewDto;
import org.sda.mediaporter.models.Gender;
import org.sda.mediaporter.models.metadata.Character;
import org.sda.mediaporter.services.CharacterService;
import org.sda.mediaporter.services.ContributorService;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.repositories.ContributorRepository;
import org.sda.mediaporter.services.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContributorServiceImpl implements ContributorService {

    private final ContributorRepository contributorRepository;
    private final GenderService genderService;

    @Autowired
    public ContributorServiceImpl(ContributorRepository contributorRepository, GenderService genderService) {
        this.contributorRepository = contributorRepository;
        this.genderService = genderService;
    }

    @Override
    public Contributor getContributorById(Long id) {
        return contributorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contributor not found"));
    }

    @Override
    public Contributor getContributorByFullName(String fullName) {
        return contributorRepository.findByFullName(fullName)
                .orElseThrow(() -> new EntityNotFoundException("Contributor not found"));
    }

    @Override
    public Page<Contributor> getAllContributors(Pageable pageable) {
        return contributorRepository.findAll(pageable);
    }

    @Override
    public Contributor getOrCreate(String fullName) {
        return null;
    }

    @Override
    public List<Contributor> getCrewsByTheMovieDbCrewsDto(List<TheMovieDbCrewDto> theMovieDbCrewsDto){
        return theMovieDbCrewsDto.stream()
                .map(this::theMovieDbCrewDtoToEntity).toList();
    }

    @Override
    public List<Contributor> getCastsByTheMovieDbCastsDto(List<TheMovieDbCastDto> theMovieDbCastsDto){
        return theMovieDbCastsDto.stream()
                .map(this::theMovieDbCastDtoToEntity).toList();
    }

    @Override
    public List<Contributor> getOrCreateCrewsByTheMovieDbCrewsDto(List<TheMovieDbCrewDto> theMovieDbCrewsDto) {
        return getCrewsByTheMovieDbCrewsDto(theMovieDbCrewsDto).stream()
                .map(this::getOrCreateContributorByTheMovieDbId).toList();
    }

    @Override
    @Transactional
    public List<Contributor> getOrCreateCastsByTheMovieDbCastsDto(List<TheMovieDbCastDto> theMovieDbCastsDto) {
        List<Contributor> actors = new ArrayList<>();
        for (TheMovieDbCastDto actor : theMovieDbCastsDto){
            Contributor contributor = getOrCreateContributorAndAddCharacter(actor);
            actors.add(contributor);
        }return actors;
    }

    @Override
    public Contributor getContributorByTheMovieDbIdOrNull(Long theMovieDbId) {
        Optional<Contributor> contributorOptional = contributorRepository.findContributorByTheMovieDb(theMovieDbId);
        return contributorOptional.orElse(null);
    }

    private Contributor getOrCreateContributorAndAddCharacter(TheMovieDbCastDto actor){
        Optional<Contributor> contributorOptional = contributorRepository.findContributorByTheMovieDb(actor.getTheMovieDbId());
        Contributor contributor;
        if(contributorOptional.isPresent()){
            contributor = contributorOptional.get();
            contributorRepository.save(contributor);
        }else{
            contributor = theMovieDbCastDtoToEntity(actor);
            contributorRepository.save(contributor);
        }
        return contributor;
    }

    private Contributor getOrCreateContributorByTheMovieDbId(Contributor contributor){
        Optional<Contributor> contributorOptional = contributorRepository.findContributorByTheMovieDb(contributor.getTheMovieDbId());
        return contributorOptional.orElseGet(() -> contributorRepository.save(contributor));
    }

    private Contributor theMovieDbCrewDtoToEntity(TheMovieDbCrewDto theMovieDbCrewDto){
        Gender gender = genderService.getGenderOrNullByTitle(theMovieDbCrewDto.getGender());

        return Contributor.builder()
                .fullName(theMovieDbCrewDto.getFullName())
                .gender(gender)
                .poster(theMovieDbCrewDto.getPoster())
                .theMovieDbId(theMovieDbCrewDto.getTheMovieDbId())
                .build();
    }

    private Contributor theMovieDbCastDtoToEntity(TheMovieDbCastDto theMovieDbCastDto){
        Gender gender = genderService.getGenderOrNullByTitle(theMovieDbCastDto.getGender());
        return Contributor.builder()
                .gender(gender)
                .theMovieDbId(theMovieDbCastDto.getTheMovieDbId())
                .fullName(theMovieDbCastDto.getFullName())
                .poster(theMovieDbCastDto.getPoster())
                .characters(new ArrayList<>())
                .build();
    }
}
