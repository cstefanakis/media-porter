package org.sda.mediaporter.services.impl;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfigurationServiceImplTest {

    @Autowired
    private ConfigurationService configurationService;

    @Test
    void isFileSupportFileSize_isInRange() {
        //Arrest
        Configuration configuration = Configuration.builder()
                .firstVideoSizeRange(1000.0)
                .secondVideoSizeRange(2000.0)
                .build();
        SourcePath sourcePath = SourcePath.builder()
                .configuration(configuration)
                .build();
        double fileSize = 1000.0;
        //Act
        boolean result = configurationService.isFileSupportFileSize(fileSize,sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportFileSize_isInRangeAndFirstSizeIsNull() {
        //Arrest
        Configuration configuration = Configuration.builder()
                .firstVideoSizeRange(null)
                .secondVideoSizeRange(2000.0)
                .build();
        SourcePath sourcePath = SourcePath.builder()
                .configuration(configuration)
                .build();
        double fileSize = 1000.0;
        //Act
        boolean result = configurationService.isFileSupportFileSize(fileSize,sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportFileSize_isInRangeAndSecondSizeIsNull() {
        //Arrest
        Configuration configuration = Configuration.builder()
                .firstVideoSizeRange(1000.0)
                .secondVideoSizeRange(null)
                .build();
        SourcePath sourcePath = SourcePath.builder()
                .configuration(configuration)
                .build();
        double fileSize = 1000.0;
        //Act
        boolean result = configurationService.isFileSupportFileSize(fileSize,sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportFileSize_FileSizeRangeIsNull() {
        //Arrest
        Configuration configuration = Configuration.builder()
                .firstVideoSizeRange(null)
                .secondVideoSizeRange(null)
                .build();
        SourcePath sourcePath = SourcePath.builder()
                .configuration(configuration)
                .build();
        double fileSize = 1000.0;
        //Act
        boolean result = configurationService.isFileSupportFileSize(fileSize,sourcePath);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileSupportFileSize_FileSizeRangeIsOutOfRange() {
        //Arrest
        Configuration configuration = Configuration.builder()
                .firstVideoSizeRange(2000.0)
                .secondVideoSizeRange(3000.0)
                .build();
        SourcePath sourcePath = SourcePath.builder()
                .configuration(configuration)
                .build();
        double fileSize = 1000.0;
        //Act
        boolean result = configurationService.isFileSupportFileSize(fileSize,sourcePath);
        //Assert
        assertFalse(result);
    }

    @Test
    void isFileModificationDateValid_isInRange() {
        //Arrest
        LocalDateTime fileModificationDate = LocalDateTime.now().minusDays(4);
        int validDaysBeforeNow = 5;
        //Act
        boolean result = configurationService.isFileModificationDateValid(fileModificationDate, validDaysBeforeNow);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileModificationDateValid_isNotInRange() {
        //Arrest
        LocalDateTime fileModificationDate = LocalDateTime.now().minusDays(6);
        int validDaysBeforeNow = 5;
        //Act
        boolean result = configurationService.isFileModificationDateValid(fileModificationDate, validDaysBeforeNow);
        //Assert
        assertFalse(result);
    }

    @Test
    void isFileModificationDateValid_validDatesIsNull() {
        //Arrest
        LocalDateTime fileModificationDate = LocalDateTime.now().minusDays(6);
        Integer validDaysBeforeNow = null;
        //Act
        boolean result = configurationService.isFileModificationDateValid(fileModificationDate, validDaysBeforeNow);
        //Assert
        assertTrue(result);
    }

}