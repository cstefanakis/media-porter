package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {
    @Query("""
            SELECT g FROM Gender g
            WHERE LOWER(g.title) = LOWER(:genderTitle)
            """)
    Optional<Gender> findGenderByTitle(@Param("genderTitle") String genderTitle);
}
