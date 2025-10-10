package org.sda.mediaporter.Services;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.List;

public interface FileService {
    List<Path> getVideoFiles(Path path);
    void copyFile(Path filePath, Path destinationFilePath);
    void deleteFile(Path path, String fileNameContain);
    void moveFile(Path fromFullPath, Path toFullPath);
    Path renameFile(Path filePath, String newFileName, String[] newSubdirectories);
    String getFileExtensionWithDot(Path file);
    Path createdDirectories(Path sourcePath, String[] directories);
//    Path generatedDestinationPathFromFilePath(Path filePath, String fileNameContain, String nameWithExtension);
//    void deleteSubDirectories(Path filePath, String fileNameContain);
    LocalDateTime getModificationLocalDateTimeOfPath(Path path);
    FileTime localDateTimeToFileTime(LocalDateTime localDateTime);
}
