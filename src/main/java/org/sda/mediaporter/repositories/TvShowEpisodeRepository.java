package org.sda.mediaporter.repositories;

import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.TvShowEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TvShowEpisodeRepository extends JpaRepository<TvShowEpisode, Long> {
    @Query("""
       SELECT te FROM TvShowEpisode te
       WHERE te.theMovieDbId = :theMovieDbId
       """)
    Optional<TvShowEpisode> findTvShowEpisodeByTheMovieDbId(@Param("theMovieDbId") Long theMovieDbId);

    @Query("""
            SELECT te FROM TvShowEpisode te
            JOIN te.videoFilePaths vfp
            JOIN vfp.sourcePath sp
            WHERE CONCAT(sp.path, vfp.filePath) = :filePath
            """)
    Optional<TvShowEpisode> findTvShowEpisodeByPath(@Param("filePath") String filePath);

    @Query("""
            SELECT tse.id
            FROM TvShowEpisode tse
            JOIN tse.videoFilePaths vfp
            WHERE vfp.id = :videoFilePathId
            """)
    Long findTvShowEpisodeIdByVideoFilePathId(@Param("videoFilePathId") Long videoFilePathId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            DELETE FROM TvShowEpisode tse
            WHERE tse.id = :tvShowEpisodeId
            AND tse.videoFilePaths IS EMPTY
            """)
    void deleteTvShowEpisodeWithoutVideoFilePaths(@Param("tvShowEpisodeId") Long tvShowEpisodeId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            DELETE FROM TvShowEpisode tse
            WHERE tse.videoFilePaths IS EMPTY
            """)
    void deleteTvShowEpisodesWithoutVideoFilePaths();

    @Query("""
            SELECT COUNT(tse) > 0
            FROM TvShowEpisode tse
            WHERE tse.theMovieDbId = :tvShowEpisodeTheMovieDbId
            """)
    boolean isExistTvShowEpisodeWithTheMovieDbId(@Param("tvShowEpisodeTheMovieDbId") Long tvShowEpisodeTheMovieDbId);
}
