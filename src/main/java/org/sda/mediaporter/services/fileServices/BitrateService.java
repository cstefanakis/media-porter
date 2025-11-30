package org.sda.mediaporter.services.fileServices;

import java.nio.file.Path;

public interface BitrateService {
    Integer getAudioBitrateFromPathFile(Path pathFile);
    Integer getVideoBitrateFromPathFile(Path pathFile);
}
