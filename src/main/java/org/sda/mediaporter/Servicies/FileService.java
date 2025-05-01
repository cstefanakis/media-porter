package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;

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
    Map<Codec, Language> getAudiosCodecAndLanguageFromAudioPath(Path videoPath);
}
