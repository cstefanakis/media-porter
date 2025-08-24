package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.ContributorService;
import org.sda.mediaporter.api.TheMovieDbContributor;
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
    public Contributor autoCreateContributor(String contributor) {
        TheMovieDbContributor apiContributor = new TheMovieDbContributor(contributor);
        try {
            String contributorName = apiContributor.getContributorName();
            return contributor == null || contributorName == null
                    ? null
                    : contributorRepository.findByFullName(contributor)
                    .orElseGet(() -> contributorRepository.save(Contributor.builder()
                            .fullName(contributorName)
                            .poster(apiContributor.getContributorPoster())
                            .website(apiContributor.getContributorWebsite())
                            .build())
                    );
        }catch (RuntimeException e){
            return null;
        }
    }
}
