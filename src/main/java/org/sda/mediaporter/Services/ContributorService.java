package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.Contributor;

public interface ContributorService {
    Contributor getContributorById(Long id);
    Contributor getContributorByFullName(String fullName);
    Contributor autoCreateContributor(String title);
}
