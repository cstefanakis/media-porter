package org.sda.mediaporter.services.fileServices;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.List;

public interface FileService {
    List<Path> getVideoFiles(Path path);
    void copyFile(Path filePath, Path destinationFilePath);
    void deleteFile(Path path);
    void moveFile(Path fromFullPath, Path toFullPath);
    Path renameFile(Path filePath, String newFileName, String[] newSubdirectories);
    String getFileExtensionWithDot(String fileTitle);
    String getSafeFileName(String text);
    Path createdDirectories(Path sourcePath, String[] directories);
    LocalDateTime getModificationLocalDateTimeOfPath(Path path);
    FileTime localDateTimeToFileTime(LocalDateTime localDateTime);

    String getStringWithoutDiacritics(String searchTitle);
}
