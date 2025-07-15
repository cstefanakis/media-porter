package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sda.mediaporter.models.Movie;
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
    select m from Movie m
    where (:title is null or lower(m.title) LIKE lower(concat('%', :title, '%') ))
    and (:year is null or m.year = :year)
    and (:rating is null or m.rating >= :rating)
    and (:genre is null or :genre member of m.genres)
    and (:country is null or m.country = :country)
    and (:director is null or exists (
        select d from m.directors d where lower(d.fullName) like lower(concat('%', :director, '%'))))
    and (:actor is null or exists (
        select a from m.actors a where lower(a.fullName) like lower(concat('%', :actor, '%'))))
    and (:audio is null or exists
        (select a from m.audios a where a.language = :audio))
    and (:writer is null or exists (
        select w from m.writers w where lower(w.fullName) like lower(concat('%', :writer, '%'))))
    and (:subtitle is null or exists (
        select sl from m.subtitles sl where sl.language = :subtitle))
    order by m.modificationDate desc
""")
    Page<Movie> filterMovies(Pageable page,
                            @Param("title") String title,
                            @Param("year") Integer year,
                            @Param("rating") Double rating,
                            @Param("genre") Genre genre,
                            @Param("country") String country,
                            @Param("director") String director,
                            @Param("actor") String actor,
                            @Param("audio") Language audio,
                            @Param("writer") String writer,
                            @Param("subtitle") Language subtitle);
}
