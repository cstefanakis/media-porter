package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select m from Movie m where m.path = :path")
    Optional<Movie> findByPath(@Param("path") String path);

    @Query("select m from Movie m where m.path like :path%")
    Page<Movie> findPathMovies(Pageable page,
                              @Param("path") Path path);

    @Query("select m from Movie m where m.modificationDate < :localDateTime")
    List<Movie> findMoviesOlderThan(@Param("localDateTime") LocalDateTime localDateTime);

    @Query("select m from Movie m  order by m.modificationDate DESC")
    Page<Movie> findLastFiveAddedMovies(Pageable pageable);

    @Query("select m from Movie  m order by m.rating DESC")
    Page<Movie> findTopFiveMovies(Pageable pageable);

    @Query("select m from Movie m where m.title = :title and m.year = :year")
    List<Movie> findMovieByTitleAndYear(@Param("title") String title,
                                            @Param("year") Integer year);

    @Query("""
    SELECT DISTINCT m FROM Movie m
    LEFT JOIN m.genres g
    LEFT JOIN m.countries c
    LEFT JOIN m.audios a
    WHERE (:title IS NULL 
        OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))
        OR LOWER(m.originalTitle) LIKE LOWER(CONCAT('%', :title, '%')))
        AND (:year IS NULL OR m.year >= :year)
        AND (:rating IS NULL OR m.rating >= :rating)
        AND (:genreIds IS NULL OR g.id IN :genreIds)
        AND (:countryIds IS NULL OR c.id IN :countryIds)
        AND (:aLanguageIds IS NULL OR a.language.id IN :aLanguageIds)
    """)
    Page<Movie> filterMovies(
            Pageable page,
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("rating") Double rating,
            @Param("genreIds") List<Long> genreIds,
            @Param("countryIds") List<Long> countryIds,
            @Param("aLanguageIds") List<Long> aLanguageIds
    );

    @Query("""
    SELECT DISTINCT m FROM Movie m
    JOIN m.audios a
        WHERE a.language.id IN :aLanguageIds
    """)
    Page<Movie> filterByAudioLanguage(Pageable page, @Param("aLanguageIds") List<Long> aLanguageIds);
}
