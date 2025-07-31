package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query ("""
            SELECT u FROM User u
            WHERE LOWER(u.username) = LOWER(:username) 
                OR LOWER(u.email) = LOWER(:email)
            """)
    Optional<User> findByUsernameOrEmail(@Param("username") String username,
                                         @Param("email") String email);
}
