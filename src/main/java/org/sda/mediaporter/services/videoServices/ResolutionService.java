package org.sda.mediaporter.services.videoServices;

import org.sda.mediaporter.models.metadata.Resolution;

import java.util.List;

public interface ResolutionService {
    Resolution getResolutionByName(String name);
    List<Resolution> getAllResolutions();
    Resolution getResolutionById(Long id);
}
