package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Role;
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
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void loadData(){
        testDataFactory.createRoleAdministrator();
        testDataFactory.createRoleUser();
    }

    @Test
    void findByName_True() {
        //Arrest
        String roleName = "Administrator";
        //Act
        Optional<Role> result = roleRepository.findByName(roleName);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findByName_False() {
        //Arrest
        String roleName = "Guest";
        //Act
        Optional<Role> result = roleRepository.findByName(roleName);
        //Assert
        assertFalse(result.isPresent());
    }
}