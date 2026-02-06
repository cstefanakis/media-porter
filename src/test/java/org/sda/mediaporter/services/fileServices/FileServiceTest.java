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
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        //Arrest
        String fileName = "videoFile.mp4";
        //Act
        String result = fileService.getFileExtensionWithDot(fileName);
        //Assert
        assertNotNull(result);
        assertEquals(".mp4", result);
    }

    @Test
    void getSafeFileName(){
        //Arrest
        String fileName = "(video)-file-1988";
        //Act
        String result = fileService.getSafeFileName(fileName);
        //Assert
        assertNotNull(result);
        assertEquals("video file 1988", result);
    }

    @Test
    void createdDirectories(){
        String[] subDirs = new String[]{"movies", "titanic"};
        //Act
        fileService.createdDirectories(this.tempDir, subDirs);
        //Assert
        assertTrue(Files.exists(Path.of(this.tempDir + File.separator + "movies" + File.separator + "titanic")));
    }

    @Test
    void getModificationLocalDateTimeOfPath() throws IOException {
        //Arrest
        LocalDateTime now = LocalDateTime.now();
        Path filePath = this.tempDir.resolve("filePath.mp4").normalize();
        Files.createFile(filePath);
        Files.setLastModifiedTime(filePath, toFileTime(now));
        //Act
        LocalDateTime result = fileService.getModificationLocalDateTimeOfPath(filePath);
        //Assert
        assertNotNull(result);
        assertEquals(now, result);
    }

    @Test
    void localDateTimeToFileTime() {
        //Arrest
        LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 1, 20, 20, 20);
        //Act
        FileTime result = fileService.localDateTimeToFileTime(localDateTime);
        //Assert
        assertEquals("2025-01-01T19:20:20Z", result.toString());
    }

    @Test
    void getStringWithoutDiacritics() {
        //Arrest
        String fileName = "Parní pračky";
        //Act
        String result = fileService.getStringWithoutDiacritics(fileName);
        //Assert
        assertNotNull(result);
        assertEquals("Parni pracky", result);
    }

    @Test
    void isFilePathExist() throws IOException {
        //Arrest
        Path filePath = this.tempDir.resolve("filePath.mp4").normalize();
        Files.createFile(filePath);
        //Act
        boolean result = fileService.isFilePathExist(filePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void deleteAllFilesInDirectory() throws IOException {
        //Arrest
        Path filePath1 = this.tempDir.resolve("filePath.mp4").normalize();
        Path filePath2 = this.tempDir.resolve("subdirectory" + File.separator + "filePath.mp4").normalize();
        Path dirPath = this.tempDir.resolve("subdirectory").normalize();
        Files.createFile(filePath1);
        Files.createDirectory(dirPath);
        Files.createFile(filePath2);
        //Act
        fileService.deleteAllFilesInDirectory(this.tempDir);
        //Assert
        assertFalse(Files.exists(filePath1));
        assertFalse(Files.exists(filePath2));
        assertTrue(Files.exists(dirPath));
    }

    @Test
    void isDirectoryEmpty() {
        //Act
        boolean result = fileService.isDirectoryEmpty(this.tempDir);
        //Assert
        assertTrue(result);
    }

    @Test
    void getAllEmptyDirectories() throws IOException {
        //Arrest
        Path filePath1 = this.tempDir.resolve("filePath.mp4").normalize();
        Path filePath2 = this.tempDir.resolve("directory" + File.separator + "filePath.mkv");
        Path directory = this.tempDir.resolve("directory");
        Path emptyDirectory = this.tempDir.resolve("subdirectory").normalize();
        Files.createDirectory(directory);
        Files.createFile(filePath2);
        Files.createFile(filePath1);
        Files.createDirectory(emptyDirectory);
        //Act
        List<Path> result = fileService.getAllEmptyDirectories(this.tempDir);
        //Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(emptyDirectory));
        assertFalse(result.contains(directory));
    }

    @Test
    void deleteAllEmptyDirectories() throws IOException {
        //Arrest
        Path filePath1 = this.tempDir.resolve("filePath.mp4").normalize();
        Path filePath2 = this.tempDir.resolve("directory" + File.separator + "filePath.mkv");
        Path directory = this.tempDir.resolve("directory");
        Path emptyDirectory = this.tempDir.resolve("subdirectory").normalize();
        Files.createDirectory(directory);
        Files.createFile(filePath2);
        Files.createFile(filePath1);
        Files.createDirectory(emptyDirectory);
        //Act
        fileService.deleteAllEmptyDirectories(this.tempDir);
        //Assert
        assertTrue(Files.exists(directory));
        assertFalse(Files.exists(emptyDirectory));
    }

    @Test
    void deleteAllDirectoriesWithoutVideFiles() throws IOException {
        //Arrest
        Path dir = this.tempDir.resolve("directory").normalize();
        Files.createDirectory(dir);
        Path videoFileDir = this.tempDir.resolve(dir).resolve("filePath.mp4").normalize();
        Files.createFile(videoFileDir);
        Path textFileDir = this.tempDir.resolve(dir).resolve("filePath.txt").normalize();
        Files.createFile(textFileDir);
        Path dir2 = this.tempDir.resolve("directory2").normalize();
        Files.createDirectory(dir2);
        Path textFileDir2 = this.tempDir.resolve(dir2).resolve("filePath.txt").normalize();
        Files.createFile(textFileDir2);
        //Act
        fileService.deleteAllDirectoriesWithoutVideFiles(this.tempDir);
        //Assert
        assertTrue(Files.exists(textFileDir));
        assertFalse(Files.exists(textFileDir2));
    }

    @Test
    void getVideoFilesUntil() throws IOException {
        //Arrest
        Path videoFile1 = this.tempDir.resolve("filePath.mp4").normalize();
        Files.createFile(videoFile1);
        Files.setLastModifiedTime(videoFile1, toFileTime(LocalDateTime.now().minusDays(2)));
        Path videoFile2 = this.tempDir.resolve("filePath.mkv").normalize();
        Files.createFile(videoFile2);
        Files.setLastModifiedTime(videoFile2, toFileTime(LocalDateTime.now()));
        //Act
        List <Path> result = fileService.getVideoFilesUntil(this.tempDir, LocalDateTime.now().minusDays(1));
        //Assert
        assertNotNull(result);
        assertTrue(result.contains(videoFile2));
        assertFalse(result.contains(videoFile1));
    }

    @Test
    void getFileSizeInMB() throws IOException {
        //Arrest
        Path file = tempDir.resolve("test.txt");
        byte[] data = new byte[1024 * 1024];
        Files.write(file, data);
        //Act
        double result = fileService.getFileSizeInMB(file);
        //Assert
        assertEquals(1, result);
    }

    @Test
    void setLastModifiedTimeToFilePath() throws IOException {
        //Arrest
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(7);
        FileTime fileTime = toFileTime(localDateTime);
        Path videoFile1 = this.tempDir.resolve("filePath.mp4").normalize();
        Files.createFile(videoFile1);
        //Act
        fileService.setLastModifiedTimeToFilePath(videoFile1, localDateTime);
        //Assert
        assertEquals(fileTime, Files.getLastModifiedTime(videoFile1));
    }

    private FileTime toFileTime(LocalDateTime localDateTime){
        return FileTime.from(
                localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}