package org.sda.mediaporter.Services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.ResolutionDto;
import org.sda.mediaporter.models.metadata.Resolution;

import java.util.List;

public interface ResolutionService {
    Resolution getResolutionByName(String name);
    List<Resolution> getAllResolutions();
    Resolution createResolution(@Valid ResolutionDto resolutionDto);
    Resolution autoCreateResolution(String resolution);
    void updateResolution(Long ResolutionId, ResolutionDto resolutionDto);
    Resolution getResolutionById(Long id);
    void deleteResolution(Long id);
}
