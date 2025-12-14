package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.metadata.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoFilePathRepository extends JpaRepository<VideoFilePath, Long> {
    @Query("""
            SELECT vfp FROM VideoFilePath vfp
            WHERE vfp.filePath = :filePath
            """)
    Optional<VideoFilePath> findVideoFilePathByPath(@Param("filePath") String filePath);

    @Query("""
            SELECT vfp FROM VideoFilePath vfp
            WHERE vfp.filePath = :filePath AND vfp.sourcePath = :sourcePath
            """)
    Optional<VideoFilePath> findVideoFilePathByPathAndSourcePath(@Param("filePath") String filePath,
                                                                 @Param("sourcePath") SourcePath sourcePath);
}
