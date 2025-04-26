package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select m from Movie m where m.path = :path")
    Optional<Movie> findByPath(@Param("path") String path);
}
