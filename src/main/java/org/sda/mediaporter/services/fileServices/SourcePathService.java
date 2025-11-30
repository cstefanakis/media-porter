package org.sda.mediaporter.services.fileServices;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.SourcePathDto;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;

import java.nio.file.Path;
import java.util.List;

public interface SourcePathService {
    SourcePath getById(Long id);
    List<SourcePath> getSourcePaths();
    List<SourcePath> getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType pathType,
                                                            LibraryItems libraryItems);
    SourcePath getSourcePathByPath(Path filePath);
    void deleteById(Long id);
    String getRootFileFromFile(String filePath);
    SourcePath createSourcePath(@Valid SourcePathDto sourcePathDto);
    SourcePath updateSourcePath(Long id, SourcePathDto sourcePathDto);
}

