package org.sda.mediaporter.repositories.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.metadata.Resolution;
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
class ResolutionRepositoryTest {

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Resolution hd;

    @BeforeEach
    void dataLoad(){
        this.hd = testDataFactory.createResolutionHd();
    }

    @Test
    void findByName_true() {
        //Arrest
        String name = this.hd.getName();
        //Act
        Optional<Resolution> result = resolutionRepository.findByName(name);
        //Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
    }

    @Test
    void findByName_false() {
        //Arrest
        String name = "noName";
        //Act
        Optional<Resolution> result = resolutionRepository.findByName(name);
        //Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }
}