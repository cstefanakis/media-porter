package org.sda.mediaporter.dataLoader;

import org.sda.mediaporter.repositories.ContributorRepository;
import org.sda.mediaporter.repositories.GenreRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final LanguageRepository languageRepository;
    private final GenreRepository genreRepository;
    private final ContributorRepository contributorRepository;

    @Autowired
    public DataLoader(LanguageRepository languageRepository, GenreRepository genreRepository, ContributorRepository contributorRepository) {
        this.languageRepository = languageRepository;
        this.genreRepository = genreRepository;
        this.contributorRepository = contributorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
