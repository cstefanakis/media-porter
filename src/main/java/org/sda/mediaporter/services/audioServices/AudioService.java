package org.sda.mediaporter.services.audioServices;

import org.sda.mediaporter.models.metadata.Audio;

import java.nio.file.Path;
import java.util.List;

public interface AudioService {
    List<Audio> getCreatedAudiosFromPathFile(Path filePath);
    List<Audio> getAudiosFromPathFile(Path filePath);
}
