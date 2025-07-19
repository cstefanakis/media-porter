package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SourcePathRepository extends JpaRepository<SourcePath, Long>  {
    @Query("select sp from SourcePath sp where sp.path = :path")
    Optional<SourcePath> findByPath(@Param("path") String path);

    @Query ("select sp from SourcePath sp where lower(trim(sp.title)) = lower(trim(:title))")
    Optional <SourcePath> findByTitle(@Param("title") String title);

    @Query ("select sp from SourcePath sp where sp.pathType = :pathType")
    List<SourcePath> findSourcePathsByPathType(@Param("pathType") SourcePath.PathType pathType);

    @Query("select sp from SourcePath sp where sp.pathType = :pathType and sp.libraryItem = :libraryItem")
    Optional<SourcePath> findSourcePathByPathTypeAndMediaType(@Param("pathType") SourcePath.PathType pathType,
                                                              @Param("libraryItem") LibraryItems libraryItem);
}
