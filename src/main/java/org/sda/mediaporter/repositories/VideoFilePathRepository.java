package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.VideoFilePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoFilePathRepository extends JpaRepository<VideoFilePath, Long> {
    @Query("""
            SELECT vfp FROM VideoFilePath vfp
            WHERE vfp.filePath = :videoFilePath
            """)
    Optional<VideoFilePath> findVideoFilePathByPath(@Param("videoFilePath") String videoFilePath);
}
