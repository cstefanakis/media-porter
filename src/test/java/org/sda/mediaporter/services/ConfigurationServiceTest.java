package org.sda.mediaporter.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.services.impl.ConfigurationServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

    @InjectMocks
    private ConfigurationServiceImpl configurationService;

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