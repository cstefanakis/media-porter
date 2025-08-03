package org.sda.mediaporter.Services.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.Services.AuthService;
import org.sda.mediaporter.dtos.LoginDto;
import org.sda.mediaporter.models.Role;
import org.sda.mediaporter.models.User;
import org.sda.mediaporter.repositories.RoleRepository;
import org.sda.mediaporter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class AuthServiceImplTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
        Role roleAdmin = roleRepository.save(Role.builder()
                .name("ROLE_ADMIN")
                .build());

        Role roleUser = roleRepository.save(Role.builder()
                .name("ROLE_USER")
                .build());

        userRepository.save(User.builder()
                .email("admin@gmail.com")
                .username("admin")
                .password(new BCryptPasswordEncoder().encode("admin"))
                .roles(Set.of(roleUser, roleAdmin))
                .build());

        userRepository.save(User.builder()
                .email("user@gmail.com")
                .username("user")
                .password(new BCryptPasswordEncoder().encode("user"))
                .roles(Set.of(roleUser))
                .build());
    }

    @Test
    void login_asAdminSuccessfully() {
        //Arrest
        LoginDto adminLogin = LoginDto.builder()
                .usernameOrEmail("admin")
                .password("admin")
                .build();

        //Act
        String token = authService.login(adminLogin);

        //Assert
        assertNotNull(token);
    }

    @Test
    void login_asAdminBadCredentials() {
        //Arrest
        LoginDto adminLogin = LoginDto.builder()
                .usernameOrEmail("admin")
                .password("wrong pass")
                .build();

        //Assert and Act
        assertThrows(BadCredentialsException.class, () -> authService.login(adminLogin));
    }

    @Test
    void login_asUserSuccessfully() {
        //Arrest
        LoginDto userLogin = LoginDto.builder()
                .usernameOrEmail("user")
                .password("user")
                .build();

        //Act
        String token = authService.login(userLogin);

        //Assert
        assertNotNull(token);
    }

    @Test
    void login_userTokenNotSameWithAdminToken() {
        //Arrest
        LoginDto userLogin = LoginDto.builder()
                .usernameOrEmail("user")
                .password("user")
                .build();

        LoginDto adminLogin = LoginDto.builder()
                .usernameOrEmail("admin")
                .password("admin")
                .build();

        //Act
        String adminToken = authService.login(adminLogin);
        String userToken = authService.login(userLogin);

        //Assert
        assertNotEquals(adminToken, userToken);
    }
}