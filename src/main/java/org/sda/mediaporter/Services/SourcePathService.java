package org.sda.mediaporter.Services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.SourcePathDto;
import org.sda.mediaporter.models.SourcePath;

import java.util.List;

public interface SourcePathService {
    SourcePath getById(Long id);
    List<SourcePath> getSourcePaths();
    void deleteById(Long id);
    SourcePath createSourcePath(@Valid SourcePathDto sourcePathDto);
    SourcePath updateSourcePath(Long id, SourcePathDto sourcePathDto);
}

