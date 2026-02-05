package org.sda.mediaporter.services.fileServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.sda.mediaporter.services.fileServices.impl.FileServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private SourcePathRepository sourcePathRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    @TempDir
    Path tempDir;

    @TempDir
    Path destinationTempDir;

    private SourcePath downloadTempSourcePath;
    private SourcePath destinationTempSourcePath;

    @BeforeEach
    void loadData(){
        this.downloadTempSourcePath = SourcePath.builder()
                .title("temporary source Path")
                .libraryItem(LibraryItems.MOVIE)
                .pathType(SourcePath.PathType.DOWNLOAD)
                .path(tempDir.toString())
                .build();

        this.destinationTempSourcePath = SourcePath.builder()
                .title("destination temporary source Path")
                .libraryItem(LibraryItems.MOVIE)
                .pathType(SourcePath.PathType.SOURCE)
                .path(destinationTempDir.toString())
                .build();
    }

    @Test
    void getVideoFiles() throws IOException {
        //Arrest
        Files.createFile(tempDir.resolve("videoFile1.avi"));
        Files.createFile(tempDir.resolve("videoFile2.mp4"));
        Files.createFile(tempDir.resolve("videoFile3.mkv"));
        Files.createFile(tempDir.resolve("videoFile4.ts"));
        Files.createFile(tempDir.resolve("textFile.txt"));
        Files.createFile(tempDir.resolve("imageFile.jpg"));
        //Act
        List<Path> result = fileService.getVideoFiles(this.tempDir);
        //Assert
        assertNotNull(result);
        assertEquals(4, result.size());
    }

    @Test
    void copyFile() throws IOException {
        //Arrest
        Path filePath = this.tempDir.resolve("videoFile.mp4");
        Files.createFile(filePath);

        Path destinationFilePath = this.destinationTempDir.resolve("videoFile.mp4");
        //Act
        fileService.copyFile(filePath, destinationFilePath);
        //Assert
        assertTrue(Files.exists(destinationFilePath));
    }

    @Test
    void deleteFile() throws IOException {
        //Arrest
        Path filePath = this.tempDir.resolve("videoFile.mp4");
        Files.createFile(filePath);
        //Act
        fileService.deleteFile(filePath);
        //Assert
        assertFalse(Files.exists(filePath));
    }

    @Test
    void moveFile() throws IOException {
        //Arrest
        when(sourcePathRepository.findAll()).thenReturn(List.of(this.destinationTempSourcePath, this.downloadTempSourcePath));
        Path filePath = this.tempDir.resolve("videoFile.mp4");
        Files.createFile(filePath);

        Path destinationFilePath = this.destinationTempDir.resolve("videoFile.mp4");
        //Act
        fileService.moveFile(filePath, destinationFilePath);
        //Assert
        assertTrue(Files.exists(destinationFilePath));
        assertFalse(Files.exists(filePath));
    }

    @Test
    void renameFile() throws IOException {
        //Arrest
        Path filePath = this.tempDir.resolve("videoFile.mp4");
        Files.createFile(filePath);
        String newName = "video";
        String subdirectory = "movie";
        Path renamedPath = this.tempDir.resolve(subdirectory + File.separator + newName + ".mp4");
        String[] subdirectories = new String[]{subdirectory};
        //Act
        fileService.renameFile(filePath, newName, subdirectories);
        //Assert
        assertTrue(Files.exists(renamedPath));
        assertFalse(Files.exists(filePath));
    }

    @Test
    void deleteSubDirectories() throws IOException {
        //Arrest
        when(sourcePathRepository.findAll()).thenReturn(List.of(this.destinationTempSourcePath, this.downloadTempSourcePath));
        String subdirectory = "movie";
        Path filePath = this.tempDir.resolve(subdirectory + File.separator + "videoFile.mp4");
        Path dirPath = filePath.getParent();
        Files.createDirectory(dirPath);
        Files.createFile(filePath);
        //Act
        Files.delete(filePath);
        fileService.deleteSubDirectories(filePath);
        //Assert
        assertFalse(Files.exists(filePath));
        assertFalse(Files.exists(filePath.getParent()));
    }

    @Test
    void getFileExtensionWithDot() {
    }

    @Test
    void getSafeFileName() {
    }

    @Test
    void createdDirectories() {
    }

    @Test
    void getModificationLocalDateTimeOfPath() {
    }

    @Test
    void localDateTimeToFileTime() {
    }

    @Test
    void getStringWithoutDiacritics() {
    }

    @Test
    void isFilePathExist() {
    }

    @Test
    void deleteAllFilesInDirectory() {
    }

    @Test
    void isDirectoryEmpty() {
    }

    @Test
    void getAllEmptyDirectories() {
    }

    @Test
    void deleteAllEmptyDirectories() {
    }

    @Test
    void deleteAllDirectoriesWithoutVideFiles() {
    }

    @Test
    void getVideoFilesUntil() {
    }

    @Test
    void getFileSizeInMB() {
    }

    @Test
    void setLastModifiedTimeToFilePath() {
    }
}