package org.sda.mediaporter.repositories;

import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
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
public interface VideoFilePathRepository extends JpaRepository<VideoFilePath, Long> {
    @Query("""
            SELECT vfp
            FROM VideoFilePath vfp
            WHERE vfp.filePath = :filePath
            """)
    Optional<VideoFilePath> findVideoFilePathByPath(@Param("filePath") String filePath);

    @Query("""
            SELECT vfp
            FROM VideoFilePath vfp
            WHERE vfp.filePath = :filePath AND vfp.sourcePath = :sourcePath
            """)
    Optional<VideoFilePath> findVideoFilePathByPathAndSourcePath(@Param("filePath") String filePath,
                                                                 @Param("sourcePath") SourcePath sourcePath);
    @Query("""
            SELECT vfp.id
            FROM VideoFilePath vfp
            WHERE
                (vfp.modificationDateTime <= :localDateTime
                OR vfp.modificationDateTime IS NULL)
                AND vfp.sourcePath = :sourcePath
            """)
    List<Long> findMoviesVideoFilePathsOlderThan(@Param("localDateTime") LocalDateTime localDateTime,
                                                 @Param("sourcePath") SourcePath sourcePath);

    @Query("""
            SELECT vfp.movie.id
            FROM VideoFilePath vfp
            WHERE vfp.id = :videoFilePathId
            """)
    Long findMovieIdByVideoFilePathId(@Param("videoFilePathId") Long videoFilePathId);

    @Modifying
    @Query("""
            DELETE FROM VideoFilePath vfp
            WHERE vfp.filePath IS NULL
            """)
    void deleteVideoFilePathsWithNullFilePath();

    @Query("""
            SELECT vfp
            FROM VideoFilePath vfp
            WHERE vfp.sourcePath.id = :sourcePathId
                AND vfp.movie IS NOT NULL
            """)
    List<VideoFilePath> findMovieVideoFilePathsBySourcePathId(Long sourcePathId);

    @Query("""
            SELECT vfp
            FROM VideoFilePath vfp
            WHERE vfp.sourcePath.id = :sourcePathId
                AND vfp.tvShowEpisode IS NOT NULL
            """)
    List<VideoFilePath> findTvShowEpisodeVideoFilePathsBySourcePathId(Long sourcePathId);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Audio a
            WHERE a.videoFilePath = :videoFilePath
            """)
    void deleteAudiosFromVideoFilePath(@Param("videoFilePath") VideoFilePath videoFilePath);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Subtitle s
            WHERE s.videoFilePath = :videoFilePath
            """)
    void deleteSubtitlesFromVideoFilePath(@Param("videoFilePath") VideoFilePath videoFilePath);


    @Query("""
            SELECT CONCAT(sp.path, vfp.filePath)
            FROM VideoFilePath vfp
            LEFT JOIN vfp.sourcePath sp
            WHERE vfp.id = :videoFilePathId
            """)
    String findStringFullPathFromVideoFilePathId(@Param("videoFilePathId") Long videoFilePathId);

    @Query("""
            SELECT vfp.id FROM VideoFilePath vfp
            LEFT JOIN vfp.sourcePath sp
            WHERE sp.libraryItem = :libraryItem
            """)
    List<Long> findTvShowsVideoFilePathIdsByLibraryItems(@Param("libraryItem") LibraryItems libraryItem);
}
