package org.sda.mediaporter.repositories;

import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
            SELECT m FROM Movie m
            JOIN m.videoFilePaths vfp
            WHERE vfp.filePath = :filePath
           """)
    Optional<Movie> findByPath(@Param("filePath") String filePath);

    @Query("""
            SELECT m FROM Movie m
            JOIN m.videoFilePaths vfp
            WHERE vfp.filePath LIKE :filePath%
           """)
    Page<Movie> findPathMovies(Pageable page,
                              @Param("filePath") Path filePath);

    @Query("""
            SELECT m FROM Movie m
            WHERE m.lastModificationDateTime < :localDateTime
           """)
    List<Movie> findMoviesOlderThan(@Param("localDateTime") LocalDateTime localDateTime);

    @Query("""
            SELECT m FROM Movie m
            ORDER by m.lastModificationDateTime DESC
           """)
    Page<Movie> findLastFiveAddedMovies(Pageable pageable);

    @Query("""
            SELECT m FROM Movie m
            ORDER BY m.rate DESC
           """)
    Page<Movie> findTopFiveMovies(Pageable pageable);

    @Query("""
            SELECT m FROM Movie m
            WHERE m.title = :title AND m.year = :year
           """)
    List<Movie> findMovieByTitleAndYear(@Param("title") String title,
                                            @Param("year") Integer year);


    @Query("""
    SELECT m FROM Movie m
        JOIN m.genres g
        JOIN m.countries c
        JOIN m.videoFilePaths vfp
        JOIN vfp.audios a
    WHERE (:title IS NULL
        OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))
        OR LOWER(m.originalTitle) LIKE LOWER(CONCAT('%', :title, '%')))
        AND (:year IS NULL OR m.year >= :year)
        AND (:rate IS NULL OR m.rate >= :rate)
        AND (:genreIds IS NULL OR g.id IN :genreIds)
        AND (:countryIds IS NULL OR c.id IN :countryIds)
        AND (:aLanguageIds IS NULL OR a.language.id IN :aLanguageIds)
    """)
    Page<Movie> filterMovies(
            Pageable page,
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("rate") Double rate,
            @Param("genreIds") List<Long> genreIds,
            @Param("countryIds") List<Long> countryIds,
            @Param("aLanguageIds") List<Long> aLanguageIds
    );

    @Query("""
    SELECT m FROM Movie m
        JOIN m.videoFilePaths vfp
        JOIN vfp.audios a
        WHERE a.language.id IN :aLanguageIds
    """)
    Page<Movie> filterByAudioLanguage(Pageable page, @Param("aLanguageIds") List<Long> aLanguageIds);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Movie m
            WHERE m.videoFilePaths IS EMPTY
            """)
    void deleteMoviesWithoutVideoFilePaths();

    @Query("""
            SELECT m FROM Movie m
            JOIN m.videoFilePaths v
            WHERE v.sourcePath = :sourcePath
            """)
    Page<Movie> findMoviesBySourcePath(Pageable page, @Param("sourcePath") SourcePath sourcePath);

    @Query("""
            SELECT m FROM Movie m
            WHERE m.theMovieDbId = :theMovieDbId
            """)
    Optional<Movie> findMovieByTheMovieDbId(@Param("theMovieDbId") Long theMovieDbId);

    @Query("""
           SELECT m.id FROM Movie m
           WHERE m.lastModificationDateTime <= :date
           """)
    List<Long> findMoviesOlderThanXDays(@Param("date") LocalDateTime date);

    @Query("""
            SELECT SIZE(m.videoFilePaths)
            FROM Movie m
            WHERE m.id = :movieId
            """)
    int findMovieVideoFilePathsSizeByMovieId(@Param("movieId") Long movieId);

    @Transactional
    @Modifying
    @Query("""
        DELETE FROM Character c
        WHERE c.movie.id IN (
            SELECT m.id FROM Movie m
            WHERE m.videoFilePaths IS EMPTY
        )
    """)
    void deleteCharactersOfMoviesWithoutVideoFilePaths();

    @Query("""
            SELECT COUNT(m) > 0
            FROM Movie m
            WHERE m.theMovieDbId = :theMovieDbId
            """)
    boolean isMovieByTheMovieDbIdExist(@Param("theMovieDbId") Long theMovieDbId);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Movie m
            WHERE m.videoFilePaths IS EMPTY
            """)
    void deleteMovieWithoutVideoFilePathsByMovieId(Long movieId);
}
