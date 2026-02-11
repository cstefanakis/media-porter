package org.sda.mediaporter.services.tvRecordsServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.services.fileServices.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TvRecordServiceTest {

    @Autowired
    private TvRecordService tvRecordService;

    @Autowired
    private FileService fileService;

    @Test
    void getFilteredFileName() {
        //Arrest
        String fileName = "[K] Πέππα, το Γουρουνάκι (Peppa Pig)2025-10-1909-10.mkv";
        String expected = "Πέππα, το Γουρουνάκι.mkv";
        //Act
        String result = tvRecordService.getFilteredFileName(fileName);
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void getFilteredDirectoryName() {
        //Arrest
        String fileName = "[K] Πέππα, το Γουρουνάκι (Peppa Pig)2025-10-1909-10.mkv";
        String extensionWithDot = fileService.getFileExtensionWithDot(fileName);
        String expected = "Πέππα, το Γουρουνάκι";
        //Act
        String result = tvRecordService.getFilteredDirectoryName(fileName, extensionWithDot);
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void getMovieDestinationPath() {
    }

    @Test
    void getTvShowsDestinationPath() {
    }
}