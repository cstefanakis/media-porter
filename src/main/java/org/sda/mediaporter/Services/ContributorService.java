package org.sda.mediaporter.Services;

import org.sda.mediaporter.dtos.ContributorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sda.mediaporter.models.Contributor;

public interface ContributorService {
    Contributor getContributorById(Long id);
    Contributor getContributorByFullName(String fullName);
    Page<Contributor> getAllContributors(Pageable pageable);
    Contributor autoCreateContributor(ContributorDto contributorDto);
}
