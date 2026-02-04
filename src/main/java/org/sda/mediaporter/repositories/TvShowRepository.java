package org.sda.mediaporter.repositories;

import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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

    @Query("""
            SELECT vfp.id FROM VideoFilePath vfp
            LEFT JOIN vfp.tvShowEpisode te
            LEFT JOIN te.tvShow ts
            WHERE ts.lastModificationDateTime <= :localDateTime
            AND vfp.sourcePath = :sourcePath
            """)
    List<Long> findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(@Param("localDateTime") LocalDateTime localDateTime,
                                                                                  @Param("sourcePath") SourcePath sourcePath);

    @Query("""
            SELECT ts.id FROM TvShow ts
            LEFT JOIN ts.tvShowEpisodes tse
            LEFT JOIN tse.videoFilePaths vfp
            WHERE vfp.id = :videoFilePathId
            """)
    Long findTvShowIdByVideoFilePathId(@Param("videoFilePathId") Long videoFilePathId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            DELETE FROM TvShow ts
            WHERE ts.tvShowEpisodes IS EMPTY
            AND ts.id = :tvShowId
            """)
    void deleteTvShowWithoutTvShowEpisodes(@Param("tvShowId") Long tvShowId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            DELETE FROM TvShow ts
            WHERE ts.tvShowEpisodes IS EMPTY
            """)
    void deleteTvShowsWithoutTvShowEpisodes();
}
