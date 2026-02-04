package org.sda.mediaporter.services.tvRecordsServices;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TvRecordServiceTest {

    @Autowired
    private TvRecordService tvRecordService;

    @Test
    void getFileNameOfPath() {
        //Arrest
        Path file = Path.of("Y:\\[K] Πέππα, το Γουρουνάκι (Peppa Pig)\\[K] Πέππα, το Γουρουνάκι (Peppa Pig)2025-10-1909-10.mkv");
        String expected = "Πέππα, το Γουρουνάκι.mkv";
        //Act
        String result = tvRecordService.getFilteredFileName(file.getFileName().toString());
        //Assert
        assertEquals(expected, result);
    }
}