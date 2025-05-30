package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.SourcePath;

import java.util.List;

public interface SourcePathService {
    SourcePath getById(Long id);
    List<SourcePath> getSourcePaths();
    void deleteById(Long id);
    SourcePath createSourcePath(SourcePath sourcePath);
    SourcePath updateSourcePath(Long id, SourcePath sourcePath);
}
