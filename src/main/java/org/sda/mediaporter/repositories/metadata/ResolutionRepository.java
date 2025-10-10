package org.sda.mediaporter.repositories.metadata;

import org.sda.mediaporter.models.metadata.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Long> {
    @Query("""
        SELECT r FROM Resolution r
        WHERE lower(trim(r.name)) = lower(trim(:name))
    """)
    Optional<Resolution> findByName(@Param("name") String name);
}
