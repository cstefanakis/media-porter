package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.ContributorService;
import org.sda.mediaporter.api.TheMovieDb;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.repositories.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
                .orElseThrow(() -> new RuntimeException("Contributor not found"));
    }

    @Override
    public Contributor getContributorByFullName(String fullName) {
        return contributorRepository.findByFullName(fullName)
                .orElseThrow(() -> new RuntimeException("Contributor not found"));
    }

    @Override
    public Contributor autoCreateContributor(String fullName) {
        TheMovieDb apiContributor = new TheMovieDb(fullName);
        Optional<Contributor> contributor = contributorRepository.findByFullName(fullName);
        return contributor.orElseGet(() -> contributorRepository.save(generatedContributor(fullName)));
    }

    private Contributor generatedContributor(String fullName){
        TheMovieDb apiContributor = new TheMovieDb(fullName);
        Contributor contributor = new Contributor();
        contributor.setFullName(apiContributor.getContributorName());
        contributor.setPoster(apiContributor.getContributorPoster());
        contributor.setWebsite(apiContributor.getContributorWebsite());
        return contributor;
    }
}
