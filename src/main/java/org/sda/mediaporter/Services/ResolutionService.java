package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.metadata.Resolution;

import java.util.List;

public interface ResolutionService {
    Resolution getResolutionByName(String name);
    List<Resolution> getAllResolutions();
    Resolution createResolution(String resolutionName);
    void updateResolution(Long ResolutionId, String resolutionName);
    Resolution getResolutionById(Long id);
    void deleteResolution(Long id);
}
