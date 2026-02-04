package org.sda.mediaporter.services.audioServices;

import org.sda.mediaporter.dtos.AudioDto;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.metadata.Audio;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface AudioService {
    Set<Audio> getCreatedAudiosFromPathFile(Path filePath, VideoFilePath videoFilePath);
    Set<Audio> getAudiosFromPathFile(Path filePath, VideoFilePath videoFilePath);

    void deleteAudioById(Long audioId);
    List<AudioDto> getAudiosDetails(Path file);
}
