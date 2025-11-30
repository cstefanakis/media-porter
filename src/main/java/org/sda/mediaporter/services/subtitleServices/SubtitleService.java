package org.sda.mediaporter.services.subtitleServices;

import org.sda.mediaporter.models.metadata.Subtitle;

import java.nio.file.Path;
import java.util.List;

public interface SubtitleService {
    List<Subtitle> createSubtitleListFromFile(Path videoFilePath);
    List<Subtitle> getSubtitleListFromFile(Path videoFilePath);
}
