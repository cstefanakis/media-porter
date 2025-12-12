package org.sda.mediaporter.services.audioServices;

import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.metadata.Audio;

import java.nio.file.Path;
import java.util.List;

public interface AudioService {
    List<Audio> getCreatedAudiosFromPathFile(Path filePath, VideoFilePath videoFilePath);
    List<Audio> getAudiosFromPathFile(Path filePath, VideoFilePath videoFilePath);
}
