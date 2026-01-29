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
    void deleteSubDirectories(Path filePath);
    String getFileExtensionWithDot(String fileTitle);
    String getSafeFileName(String text);
    Path createdDirectories(Path sourcePath, String[] directories);
    LocalDateTime getModificationLocalDateTimeOfPath(Path path);
    FileTime localDateTimeToFileTime(LocalDateTime localDateTime);

    String getStringWithoutDiacritics(String searchTitle);

    boolean isFilePathExist(Path filePath);

    void deleteAllFilesInDirectory(Path directory);

    boolean isDirectoryEmpty(Path path);

    List <Path> getAllEmptyDirectories(Path directory);

    void deleteAllEmptyDirectories(Path directory);

    void deleteAllDirectoriesWithoutVideFiles(Path rootDirectory);


    List<Path> getVideoFilesUntil(Path directory, LocalDateTime maxDateCopyFiles);

    double getFileSizeInMB(Path filePath);

    void setLastModifiedTimeToFilePath(Path filePath, LocalDateTime localDateTime);
}
