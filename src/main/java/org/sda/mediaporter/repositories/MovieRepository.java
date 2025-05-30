package org.sda.mediaporter.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sda.mediaporter.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select m from Movie m where m.path = :path")
    Optional<Movie> findByPath(@Param("path") String path);

    @Query("select m from Movie m where m.path like :path%")
    Page<Movie> findPathMovies(Pageable page,
                              @Param("path") Path path);

    @Query("select m from Movie m  order by m.modificationDate DESC")
    List<Movie> findLastFiveAddedMovies(Pageable pageable);

    @Query("select m from Movie  m order by m.rating DESC")
    List<Movie> findTopFiveMovies(Pageable pageable);

    Page <Movie> findAll(Pageable pageable);
}
