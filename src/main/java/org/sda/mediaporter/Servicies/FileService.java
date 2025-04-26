package org.sda.mediaporter.Servicies;

import java.nio.file.Path;
import java.util.List;

public interface FileService {
    List<Path> files(String path);
    void scannedMoviesPath (String path);
    void copyFile(Path fromPath, Path toPath);
    void deleteFile(Path path);
    void moveFile(Path fromPath, Path pathWithoutFileName);
    void renameFile(Path filePath, String newName);
}
