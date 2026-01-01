package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.TvShowEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
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
            WHERE vfp.filePath = :filePath
            """)
    Optional<TvShowEpisode> findTvShowEpisodeByPath(@Param("filePath") String filePath);
}
