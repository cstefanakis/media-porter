package org.sda.mediaporter.services.fileServices;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Test
    void getVideoFiles() {
    }

    @Test
    void copyFile() throws IOException {
        //Arrest
        Path file = Path.of("src/test/resources/files/notitled (720p_24fps_H264-128kbit_AAC).mp4");
        Path newFilePath = Path.of("src/test/resources/movies/notitled (720p_24fps_H264-128kbit_AAC).mp4");
        //Act
        fileService.copyFile(file, newFilePath);
        //Assert
        assertTrue(Files.exists(newFilePath));
        assertTrue(Files.exists(file));
        //After
        Files.delete(newFilePath);
    }

    @Test
    void deleteFile() {
    }

    @Test
    void moveFile() {
    }

    @Test
    void renameFile() {
    }

    @Test
    void getFileExtensionWithDot() {
    }

    @Test
    void getSafeFileName() {
        //Arrest
        String fileName = "this.is&unsafe(filě)/";
        //Act
        String result = fileService.getSafeFileName(fileName);
        //Assert
        assertEquals("this is unsafe filě", result);
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
        //Arrest
        String text = "Tři oříšky pro Popelku";
        //Act
        String result = fileService.getStringWithoutDiacritics(text);
        //Assert
        assertEquals("Tri orisky pro Popelku", result);
    }

    @Test
    void deleteAllFilesInDirectory() {
        //Arrest
        Path path = Path.of("C:\\Users\\chris\\Downloads\\Movies\\Warfare (2025)");
        //Act
        fileService.deleteAllFilesInDirectory(path);
        boolean result = fileService.isDirectoryEmpty(path);
        //Assert
        assertTrue(result);
    }

    @Test
    void deleteAllEmptyDirectories() {
        //Arrest
        Path rootDirectory = Path.of("C:\\Users\\chris\\Downloads\\Movies");
        //Act
        fileService.deleteAllEmptyDirectories(rootDirectory);
        boolean result = fileService.isDirectoryEmpty(rootDirectory);
        //Assert
        assertTrue(result);
    }

    @Test
    void getFileSizeInMB() {
        //Arrest
        Path filePath = Path.of("src\\test\\resources\\files\\notitled (720p_24fps_H264-128kbit_AAC).mp4");
        //Act
        double result = fileService.getFileSizeInMB(filePath);
        //Assert
        assertTrue(result > 0);
        assertEquals(22.618480682373047, result);
    }

    @Test
    void setLastModifiedTimeToFilePath() {
        //Arrest
        Path filePath = Path.of("src\\test\\resources\\externalSources\\tvRecords\\movies\\[K12] Λούφα και Παραλλαγή.mp4");
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(5);
        //Act
        fileService.setLastModifiedTimeToFilePath(filePath, localDateTime);
        LocalDateTime result = fileService.getModificationLocalDateTimeOfPath(filePath);
        //Assert
        assertEquals(localDateTime, result);
    }
}