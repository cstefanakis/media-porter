package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Movie;

import java.nio.file.Path;
import java.util.List;

public interface FileService {
    List<Path> getVideoFiles(Path path);
    void copyFile(Path fromPath, Path toPath);
    void deleteFile(Path path);
    void moveFile(Path fromPath, Path pathWithoutFileName);
    void renameFile(Path filePath, String newName, Integer year);
    String getFileExtensionWithDot(Path file);
    Path renamedPath(Path filePath, String newName, Integer year);
}
