package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class ContributorRepositoryTest {

    @Autowired
    private ContributorRepository contributorRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Contributor director;

    @BeforeEach
    void loadData(){
        this.director = testDataFactory.createDirector();
    }

    @Test
    void findByFullName_True() {
        //Arrest
        String contributorName = director.getFullName();
        //Act
        Optional<Contributor> result = contributorRepository.findByFullName(contributorName);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findByFullName_False() {
        //Arrest
        String contributorName = "otherName";
        //Act
        Optional<Contributor> result = contributorRepository.findByFullName(contributorName);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findContributorByTheMovieDb_True() {
        //Arrest
        Long contributorTheMovieDbId = this.director.getTheMovieDbId();
        //Act
        Optional<Contributor> result = contributorRepository.findContributorByTheMovieDb(contributorTheMovieDbId);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findContributorByTheMovieDb_False() {
        //Arrest
        Long contributorTheMovieDbId = 0L;
        //Act
        Optional<Contributor> result = contributorRepository.findContributorByTheMovieDb(contributorTheMovieDbId);
        //Assert
        assertFalse(result.isPresent());
    }
}