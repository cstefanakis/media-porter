package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Repository
public interface SourcePathRepository extends JpaRepository<SourcePath, Long>  {
    @Query("""
        SELECT sp FROM SourcePath sp
        WHERE sp.path = :path
        """)
    Optional<SourcePath> findByPath(@Param("path") String path);

    @Query("""
        SELECT sp FROM SourcePath sp
        WHERE LOWER(TRIM(sp.title)) = LOWER(TRIM(:title))
        """)
    Optional <SourcePath> findByTitle(@Param("title") String title);

    @Query("""
        SELECT sp FROM SourcePath sp
        WHERE sp.pathType = :pathType
        """)
    List<SourcePath> findSourcePathsByPathType(@Param("pathType") SourcePath.PathType pathType);

    @Query("""
        SELECT sp FROM SourcePath sp
        WHERE sp.pathType = :pathType AND sp.libraryItem = :libraryItem
        """)
    List<SourcePath> findSourcePathByPathTypeAndLibraryItem(@Param("pathType") SourcePath.PathType pathType,
                                                                @Param("libraryItem") LibraryItems libraryItem);

    @Query("""
            SELECT sp.path FROM SourcePath sp
            WHERE sp.path LIKE CONCAT(:filePath, '%')
            """)
    Optional<String> findRootPathOfFile(@Param("filePath") String filePath);

    @Query("""
            SELECT sp FROM SourcePath sp
            WHERE :filePath LIKE CONCAT(sp.path, '%')
            """)
    Optional<SourcePath> findSourcePathByPath(@Param("filePath") String filePath);
}
