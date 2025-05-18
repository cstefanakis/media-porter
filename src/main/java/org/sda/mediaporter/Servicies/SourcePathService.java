package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.repositories.SourcePathRepository;

import java.util.List;

public interface SourcePathService {
    SourcePath getById(Long id);
    List<SourcePath> getSourcePaths();
    void deleteById(Long id);
    SourcePath createSourcePath(SourcePath sourcePath);
    SourcePath updateSourcePath(Long id, SourcePath sourcePath);
}
