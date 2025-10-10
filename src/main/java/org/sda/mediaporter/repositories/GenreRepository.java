package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query("""
    SELECT g FROM Genre g
        WHERE LOWER(TRIM(g.title)) = LOWER(TRIM(:title))
    """)
    Optional<Genre> findGenreByTitle(@Param("title") String title);
}
