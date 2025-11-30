package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    @Query("select c from Contributor c where c.fullName = :fullName")
    Optional<Contributor> findByFullName(@Param("fullName") String fullName);

    @Query("""
            SELECT c FROM Contributor
            WHERE c.theMovieDbId = :theMovieDbId
            """)
    Optional<Contributor> findContributorByTheMovieDb(@Param("theMovieDbId") Long theMovieDbId);
}
