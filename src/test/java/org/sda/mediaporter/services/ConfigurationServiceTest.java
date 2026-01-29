package org.sda.mediaporter.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfigurationServiceTest {

    @Autowired
    private ConfigurationService configurationService;

    @Test
    void isFileModificationDateValid_true() {
        //Arrest
        LocalDateTime filePathLocalDateTime = LocalDateTime.now();
        Integer datesSupportFile = 5;
        //Act
        boolean result = configurationService.isFileModificationDateValid(filePathLocalDateTime, datesSupportFile);
        //Assert
        assertTrue(result);
    }
    @Test
    void isFileModificationDateValid_false() {
        //Arrest
        LocalDateTime filePathLocalDateTime = LocalDateTime.now().minusDays(6);
        Integer datesSupportFile = 5;
        //Act
        boolean result = configurationService.isFileModificationDateValid(filePathLocalDateTime, datesSupportFile);
        //Assert
        assertFalse(result);
    }

    @Test
    void isFileOld_True() {
        //Arrest
        LocalDateTime fileModificationDate = LocalDateTime.now().minusDays(5);
        Integer configurationMaxDatesSave = 4;
        //Act
        boolean result = configurationService.isFileOld(fileModificationDate, configurationMaxDatesSave);
        //Assert
        assertTrue(result);
    }

    @Test
    void isFileOld_False() {
        //Arrest
        LocalDateTime fileModificationDate = LocalDateTime.now().minusDays(3);
        Integer configurationMaxDatesSave = 4;
        //Act
        boolean result = configurationService.isFileOld(fileModificationDate, configurationMaxDatesSave);
        //Assert
        assertFalse(result);
    }

}