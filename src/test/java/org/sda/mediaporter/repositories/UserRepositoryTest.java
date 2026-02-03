package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.User;
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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private User user;
    private User admin;

    @BeforeEach
    void loadData(){
        this.user = testDataFactory.createUserUser();
        this.admin = testDataFactory.createUserAdmin();
    }

    @Test
    void findByUsernameOrEmail_byEmail() {
        //Arrest
        Long userId = this.user.getId();
        String email = user.getEmail().toUpperCase();
        //Act
        Optional<User> result = userRepository.findByUsernameOrEmail(email);
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
    }

    @Test
    void findByUsernameOrEmail_byUser() {
        //Arrest
        Long adminId = this.admin.getId();
        String username = this.admin.getUsername().toUpperCase();
        //Act
        Optional<User> result = userRepository.findByUsernameOrEmail(username);
        assertTrue(result.isPresent());
        assertEquals(adminId, result.get().getId());
    }
}