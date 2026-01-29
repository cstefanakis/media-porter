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
        String result = tvRecordService.getFileNameOfPath(file);
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void getTvShowsDestinationPath() {
        //Arrest
        Path destinationPathRoot = Path.of("Y:").normalize();
        Path file = Path.of("src/test/resources/externalSources/tvRecords/tvShows/[K12] IQ 160 - Season 2 (R).mkv");
        String originalTitle = "Peppa pig";
        Integer year = 2001;
        Path destinationFile = Path.of("Y:\\Peppa pig (2001)\\Peppa pig (2026.01.26 (09_07)).mkv").normalize();
        //Act
        Path result = tvRecordService.getTvShowsDestinationPath(file, originalTitle, year, destinationPathRoot);
        //Assert
        assertEquals(destinationFile, result);
    }
}