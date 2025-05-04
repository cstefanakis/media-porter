package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Audio;
import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.models.metadata.Video;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface FileService {
    List<Path> getVideoFiles(Path path);
    void copyFile(Path fromPath, Path toPath);
    void deleteFile(Path path);
    void moveFile(Path fromPath, Path pathWithoutFileName);
    void renameFile(Path filePath, String newName, Integer year);
    String getFileExtensionWithDot(Path file);
    Path renamedPath(Path filePath, String newName, Integer year);
    List<Path> getVideoFilesOfSource(Path path);
    List<Audio> getAudiosInfoFromPath(Path videoPath);
    Video getVideoInfoFromPath(Path videoPath);
    List<Subtitle> getSubtitlesInfoFromPath(Path videoPath);
}
