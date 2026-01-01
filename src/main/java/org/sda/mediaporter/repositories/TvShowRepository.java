package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TvShowRepository extends JpaRepository<TvShow, Long> {
    @Query("""
        SELECT t FROM TvShow t
        WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))
           OR LOWER(t.originalTitle) LIKE LOWER(CONCAT('%', :title, '%'))
    """)
    Page<TvShow> findTvShowsByTitle(@Param("title") String title,
                                         Pageable pageable);

    @Query("""
            SELECT ts FROM TvShow ts
            WHERE ts.theMoveDBTvShowId = :theMoveDBTvShowId
            """)
    Optional<TvShow> findTvShowByTheMovieDBId(@Param("theMoveDBTvShowId") Long theMoveDBTvShowId);
}
