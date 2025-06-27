package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query("select g from Genre g where lower(trim(g.title)) = lower(trim(:title))")
    Optional<Genre> findGenreByTitle(@Param("title") String title);
}
