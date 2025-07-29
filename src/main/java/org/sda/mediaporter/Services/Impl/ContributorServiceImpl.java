package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.ContributorService;
import org.sda.mediaporter.api.TheMovieDbContributor;
import org.sda.mediaporter.dtos.ContributorDto;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.repositories.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContributorServiceImpl implements ContributorService {

    private final ContributorRepository contributorRepository;

    @Autowired
    public ContributorServiceImpl(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
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
    public Contributor autoCreateContributor(ContributorDto contributorDto) {

        TheMovieDbContributor apiContributor = new TheMovieDbContributor(contributorDto.getFullName());

        String contributorName = apiContributor.getContributorName();

        Optional<Contributor> contributor = contributorRepository.findByFullName(contributorName);

        if(contributor.isEmpty()){
            Contributor newContributor = generatedContributor(apiContributor);
            if(newContributor.getFullName() != null){
                return contributorRepository.save(newContributor);
            }
        }
        return null;
    }

    private Contributor generatedContributor(TheMovieDbContributor apiContributor){
        Contributor contributor = new Contributor();
        contributor.setFullName(apiContributor.getContributorName());
        contributor.setPoster(apiContributor.getContributorPoster());
        contributor.setWebsite(apiContributor.getContributorWebsite());
        return contributor;
    }
}
