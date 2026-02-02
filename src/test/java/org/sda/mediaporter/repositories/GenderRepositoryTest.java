package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Gender;
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
class GenderRepositoryTest {

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void loadData(){
        testDataFactory.createGenderFemale();
        testDataFactory.createGenderMale();
    }

    @Test
    void findGenderByTitle_true() {
        //Arrest
        String title = "male";
        //Act
        Optional<Gender> result = genderRepository.findGenderByTitle(title);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findGenderByTitle_false() {
        //Arrest
        String title = "mal";
        //Act
        Optional<Gender> result = genderRepository.findGenderByTitle(title);
        //Assert
        assertFalse(result.isPresent());
    }
}