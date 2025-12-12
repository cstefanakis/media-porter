package org.sda.mediaporter.services.fileServices;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Test
    void getVideoFiles() {
    }

    @Test
    void copyFile() {
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
}