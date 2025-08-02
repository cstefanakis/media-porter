package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sda.mediaporter.Services.ResolutionService;
import org.sda.mediaporter.dtos.ResolutionDto;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ResolutionServiceImplTest {

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ResolutionService resolutionService;

    private Resolution fullHd;
    private Resolution hd;

    @BeforeEach
    void setup(){
        videoRepository.deleteAll();
        configurationRepository.deleteAll();
        resolutionRepository.deleteAll();

        hd = resolutionRepository.save(
                Resolution.builder()
                .name("720p")
                .build()
        );

        fullHd = resolutionRepository.save(
                Resolution.builder()
                        .name("1080p")
                        .build()
        );
    }

    @Test
    void getResolutionByName_successfully() {
        //Arrest
        String resolutionName = "1080P";

        //Act
        Resolution resolution = resolutionService.getResolutionByName(resolutionName);

        //Assert
        assertEquals("1080p", resolution.getName());
    }

    @Test
    void getResolutionByName_NotExistName() {
        //Arrest
        String resolutionName = "64p";

        //Assert and Act
        assertThrows(EntityNotFoundException.class, ()-> resolutionService.getResolutionByName(resolutionName));
    }

    @Test
    void getAllResolutions() {
        //Act
        List <Resolution> resolutions = resolutionService.getAllResolutions();

        //Assert
        assertEquals(2, resolutions.size());
    }

    @Test
    void createResolution_successfully() {
        //Arrest
        ResolutionDto newResolutionDto = ResolutionDto.builder()
                .name("1440p")
                .build();

        //Act
        Resolution newResolution = resolutionService.createResolution(newResolutionDto);

        //Assert
        assertNotNull(newResolution.getId());
    }

    @Test
    void createResolution_existResolutionName() {
        //Arrest
        ResolutionDto newResolutionDto = ResolutionDto.builder()
                .name("1080p")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> resolutionService.createResolution(newResolutionDto));
    }

    @Test
    void createResolution_createWithNullName() {
        //Arrest
        ResolutionDto newResolutionDto = ResolutionDto.builder()
                .build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> resolutionService.createResolution(newResolutionDto));
    }

    @Test
    void updateResolution_successfully() {
        //Assert
        Long id = fullHd.getId();
        ResolutionDto updatedResolutionDto = ResolutionDto.builder()
                .name("1080")
                .build();


        //Act
        resolutionService.updateResolution(id, updatedResolutionDto);

        //Assert
        assertEquals("1080", resolutionService.getResolutionById(id).getName());
    }

    @Test
    void updateResolution_existName() {
        //Assert
        Long id = fullHd.getId();
        ResolutionDto updatedResolutionDto = ResolutionDto.builder()
                .name("720p")
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> resolutionService.updateResolution(id, updatedResolutionDto));
    }

    @Test
    void updateResolution_withNullName() {
        //Assert
        Long id = fullHd.getId();
        ResolutionDto updatedResolutionDto = ResolutionDto.builder().build();

        //Act
        resolutionService.updateResolution(id, updatedResolutionDto);
        Resolution updatedResolution = resolutionService.getResolutionById(id);

        //Assert
        assertEquals("1080p", updatedResolution.getName());
    }

    @Test
    void getResolutionById_successfully() {
        //Arrest
        Long id = fullHd.getId();

        //Act
        Resolution resolution = resolutionService.getResolutionById(id);

        //Assert
        assertEquals("1080p", resolution.getName());
    }

    @Test
    void getResolutionById_notExistId() {
        //Arrest
        Long id = 0L;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> resolutionService.getResolutionById(id));
    }

    @Test
    void deleteResolution_successfully() {
        //Arrest
        Long id = hd.getId();

        //Act
        resolutionService.deleteResolution(id);

        //Assert
        assertFalse(resolutionRepository.findById(id).isPresent());
    }

    @Test
    void deleteResolution_notExistId() {
        //Arrest
        Long id = 0L;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> resolutionService.getResolutionById(id));
    }
}