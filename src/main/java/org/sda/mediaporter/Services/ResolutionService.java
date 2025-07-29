package org.sda.mediaporter.Services;

import org.sda.mediaporter.dtos.ResolutionDto;
import org.sda.mediaporter.models.metadata.Resolution;

import java.util.List;

public interface ResolutionService {
    Resolution getResolutionByName(String name);
    List<Resolution> getAllResolutions();
    Resolution createResolution(ResolutionDto resolutionDto);
    void updateResolution(Long ResolutionId, ResolutionDto resolutionDto);
    Resolution getResolutionById(Long id);
    void deleteResolution(Long id);
}
