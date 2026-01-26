package org.sda.mediaporter.services.subtitleServices;

import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.metadata.Subtitle;

import java.nio.file.Path;
import java.util.List;

public interface SubtitleService {
    void createSubtitleListFromFile(Path filePath, VideoFilePath videoFilePath);
    List<Subtitle> getSubtitleListFromFile(Path filePath, VideoFilePath videoFilePath);

    void deleteSubtitleById(Long subtitleId);
}
