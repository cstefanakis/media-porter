package org.sda.mediaporter.Services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.ContributorRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class ContributorServiceTest {

    @Autowired
    private ContributorRepository contributorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ContributorService contributorService;

    private Contributor adamSandler;

    @BeforeEach
    void setup(){
        movieRepository.deleteAll();
        configurationRepository.deleteAll();
        contributorRepository.deleteAll();

        adamSandler = contributorRepository.save(
                Contributor.builder()
                .fullName("Adam Sandler")
                .build()
        );

        contributorRepository.save(
                Contributor.builder()
                        .fullName("Mel Gibson")
                        .build()
        );
    }

    @Test
    void getContributorById_noExistId() {
        //Arrest
        Long noExistId = 0L;

        //Act and Assert
        assertThrows(EntityNotFoundException.class, () -> contributorService.getContributorById(noExistId));
    }

    @Test
    void getContributorById_successfully() {
        //Arrest
        Long id = adamSandler.getId();

        //Act
        Contributor constructor = contributorService.getContributorById(id);

        //Assert
        assertEquals("Adam Sandler", constructor.getFullName());
    }

    @Test
    void getContributorByFullName() {
        //Arrest
        String fullName = "Adam Sandler";

        //Act
        Contributor contributor = contributorService.getContributorByFullName(fullName);

        //Assert
        assertEquals(adamSandler.getId(), contributor.getId());
    }

    @Test
    void getContributor_notExistContributor() {
        //Arrest
        String fullName = "no exist";

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> contributorService.getContributorByFullName(fullName));
    }

    @Test
    void getContributorByFullName_withNullFullName() {
        //Arrest
        String fullName = null;

        //Assert and Act
        assertThrows(RuntimeException.class, () -> contributorService.getContributorByFullName(fullName));
    }

    @Test
    void getAllContributors() {
        //Arrest
        Pageable pageable = PageRequest.of(0, 2);

        //Act
        Page<Contributor> contributors = contributorService.getAllContributors(pageable);

        //Assert
        assertEquals(2, contributors.getSize());
    }

    @Test
    void autoCreateContributor_successfully() {
        //Arrest
        String contributorFullName = "Dwayne Douglas Johnson";

        //Act
        Contributor autoCreatedContributor = contributorService.autoCreateContributor(contributorFullName);

        //Assert
        assertNotNull(autoCreatedContributor.getId());
    }

    @Test
    void autoCreateContributor_WithFullNameNull() {
        //Assert and Act
        assertThrows(NullPointerException.class, () -> contributorService.autoCreateContributor(null));
    }
}